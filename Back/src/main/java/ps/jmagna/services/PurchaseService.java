package ps.jmagna.services;

import ps.jmagna.clients.CustomMPClient;
import ps.jmagna.dtos.purchase.DeliveryDetailDto;
import ps.jmagna.dtos.purchase.DeliveryDto;
import ps.jmagna.dtos.purchase.SellDto;
import ps.jmagna.dtos.purchase.NotificationResponce;
import ps.jmagna.dtos.purchase.PurchaseResponce;
import ps.jmagna.dtos.purchase.SaleDetailDto;
import ps.jmagna.dtos.purchase.SaleDto;
import ps.jmagna.dtos.purchase.PurchaseItemRequest;
import ps.jmagna.dtos.purchase.PurchaseRequest;
import ps.jmagna.dtos.purchase.PutDeliveryRequest;
import ps.jmagna.entities.*;
import ps.jmagna.enums.DeliveryState;
import ps.jmagna.enums.SaleState;
import ps.jmagna.enums.SecType;
import ps.jmagna.enums.UserRole;
import ps.jmagna.repository.*;
import com.mercadopago.client.merchantorder.MerchantOrderClient;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.*;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.merchantorder.MerchantOrder;
import com.mercadopago.resources.merchantorder.MerchantOrderItem;
import com.mercadopago.resources.merchantorder.MerchantOrderPayment;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static ps.jmagna.services.AuthService.userCanBuy;

@Service
public class PurchaseService {

    @Autowired
    SaleRepository saleRepository;
    @Autowired
    SaleDetailRepository saleDetailRepository;
    @Autowired
    DeliveryRepository deliveryRepository;
    @Autowired
    PublicationRepository publicationRepository;
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    NotificationService notificationService;

    @Autowired
    PreferenceClient preferenceClient;
    @Autowired
    CustomMPClient customMPClient;
    @Autowired
    MerchantOrderClient merchantOrderClient;
    @Autowired
    PaymentClient paymentClient;

    @Autowired
    String tunnelUrl;
    @Value("${mp.access-token}")
    String accessToken;
    @Value("${mp.email-app}")
    String emailApp;
    @Value("${app.url}")
    private String url;

    //Register and Not
    @Transactional
    public PurchaseResponce registerSale(PurchaseRequest request, UserEntity user) throws MPException, MPApiException {
        if(!userCanBuy(user)){
            throw new EntityNotFoundException("El usuario no puede comprar");
        }

        List<PreferenceItemRequest> items = new ArrayList<>();
        for (PurchaseItemRequest itemRequest: request.getItems()) {
            PublicationEntity publication = publicationRepository.getReferenceById(itemRequest.getIdPub());
            if (!publication.isCanSold() && publication.getCount() <= 0) {
                throw new EntityNotFoundException("No se puede vender");
            }

            if (publication.getCount() < itemRequest.getCount()) {
                throw new EntityNotFoundException("Cantidad excede el stock actual");
            }

            PreferenceItemRequest item =
                    PreferenceItemRequest.builder()
                            .id(publication.getId().toString())
                            .title(publication.getName())
                            .description(publication.getName())
                            .pictureUrl("")
                            .categoryId("Publicacion")
                            .quantity(itemRequest.getCount())
                            .currencyId("ARS")
                            .unitPrice(publication.getPrice())
                            .build();
            items.add(item);
        }
        MPRequestOptions options = MPRequestOptions.builder()
                .accessToken(accessToken)
                .build();
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .backUrls(PreferenceBackUrlsRequest.builder()
                        .success("http://localhost:4200/explore").build())
                .notificationUrl(tunnelUrl + "/api/sell/not?user=" + user.getId())
                .items(items)
                .build();


        Preference preference = preferenceClient.create(preferenceRequest,options);

        return new PurchaseResponce(preference);
    }
    
    @Transactional
    public NotificationResponce notificar(LinkedHashMap data, String userId) throws MPException, MPApiException {
        NotificationResponce responce;
//        System.out.println(data.values());
//        System.out.println(data.keySet());

        if(!data.containsKey("resource") || !data.containsKey("topic")){
            return new NotificationResponce("","nodata");
        }

        responce = new NotificationResponce(data.get("resource").toString(),
                data.get("topic").toString());

        String path;
        try {
            URI uri = new URI(responce.getResource());
            path = uri.getPath();
        }catch (Exception e){
            path = "";
        }
//        String path =  URI.create(responce.getResource()).getPath();
        String idStr = path.substring(path.lastIndexOf('/') + 1);
        Long id = Long.parseLong(idStr);

        MerchantOrder m;
        if (responce.getTopic().equals("merchant_order")) {
            m = merchantOrderClient.get(id);
        } else {
            Payment p = paymentClient.get(id);
            m = merchantOrderClient.get(p.getOrder().getId());
        }
//            System.out.println("+Merchandorder: "+m.getId());

        Optional<SaleEntity> optionalSale = saleRepository.findByMerchantOrder(m.getId());
        System.out.println("found"+optionalSale.isPresent());
        SaleEntity sale = optionalSale.orElseGet(() -> registerSaleDelivery(userId, m));

        BigDecimal total = BigDecimal.ZERO;
        for (MerchantOrderPayment mpay : m.getPayments()) {
            if (mpay.getStatus().equals("approved")) {
                total = total.add(mpay.getTotalPaidAmount());
            }
        }

        if (total.compareTo(m.getTotalAmount()) >= 0) {
            if (m.getShipments().isEmpty()) { // The merchant_order don't has any shipments
                System.out.println("Totally paid. Release your item.");

                if(sale.getSaleState().equals(SaleState.APROBADA)){
                    notificationService.sendNotificationSale(sale);
                }
                sale.setSaleState(SaleState.APROBADA);
                saleRepository.saveAndFlush(sale);
            }
        }
        return responce;
    }

    private SaleEntity registerSaleDelivery(String userId, MerchantOrder m) {
        SaleEntity sale;
        sale = new SaleEntity();
        sale.setDateTime(LocalDateTime.now());
        Long uId = Long.parseLong(userId);

        UserEntity user = userRepository.getReferenceById(uId);
        if(!userCanBuy(user)){
            throw new EntityNotFoundException("El usuario no puede comprar");
        }
        sale.setUser(user);
        sale.setSaleState(SaleState.PENDIENTE);
        sale.setMerchantOrder(m.getId());


        List<SaleDetailEntity> saleDetails = new ArrayList<>();
        for (MerchantOrderItem item : m.getItems()) {
            PublicationEntity publication =
                    publicationRepository.getReferenceById(Long.parseLong(item.getId()));

            SaleDetailEntity detail = new SaleDetailEntity(null,
                    sale,
                    publication,
                    item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())),
                    item.getQuantity());
            saleDetails.add(detail);

            publication.setCount(publication.getCount() - item.getQuantity());
            publicationRepository.saveAndFlush(publication);
        }
        sale.setDetails(saleDetails);

        sale = saleRepository.saveAndFlush(sale);

        DeliveryEntity delivery = new DeliveryEntity();
        delivery.setId(sale.getId());
        delivery.setSale(sale);
        delivery.setShipmment(m.getId());
        delivery.setDealer(getDeliveryFree());
        delivery.setDeliveryState(DeliveryState.PENDIENTE);
        deliveryRepository.save(delivery);
        return sale;
    }
    
    //Get
    UserEntity getDeliveryFree() {
        UserEntity selected = null;
        Long countSelected = 0L;
        for (UserEntity user : userRepository.findAllByRole(UserRole.DELIVERY)) {
            Long count = deliveryRepository.countAllByDealer(user);
            if (count < countSelected || selected==null) {
                selected = user;
                countSelected = count;
            }
        }



        return selected;
    }
    public List<SaleDto> getPurchases(String firstDate, String lastDate, String pubName, UserEntity user) {
        List<SaleDto> list = new ArrayList<>();
        for (SaleEntity sale : saleRepository.findAllByDateTimeBetweenAndUser(
                LocalDateTime.parse(firstDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                LocalDateTime.parse(lastDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                user
        )) {
//            if (!user.equals(sale.getUser().getId())) {
//                continue;
//            }
            BigDecimal total = BigDecimal.ZERO;
            List<SaleDetailDto> detailDtos = new ArrayList<>();
            boolean addDto = false;
            for (SaleDetailEntity detail : sale.getDetails()) {
                if(pubName.isBlank() || detail.getPublication().getName().toLowerCase()
                        .contains(pubName.toLowerCase().toLowerCase())) {
                    addDto = true;
                }
                Long imgId = 1L;
                for (SectionEntity s : detail.getPublication().getSections()) {
                    if (s.getType() == SecType.PHOTO) {
                        imgId = s.getId();
                        break;
                    }
                }

                detailDtos.add(new SaleDetailDto(
                        detail.getPublication().getId(),
                        detail.getPublication().getName(),
                        url + "/api/image/pub/" + imgId,
                        detail.getTotal(),
                        detail.getCount()
                ));
                total = total.add(detail.getTotal());
            }

            if(addDto){
                list.add(new SaleDto(sale.getId(),
                        sale.getDateTime().toString(),
                        detailDtos,
                        sale.getSaleState(),
                        total)
                );
            }
        }

        list.sort(Comparator.comparing(SaleDto::getDateTime).reversed());
        return list;

    }
    public List<DeliveryDto> getDeliveries(DeliveryState state, UserEntity user) {
        List<DeliveryDto> list = new ArrayList<>();
        List<DeliveryEntity> entities;
        if(user.getRole().equals(UserRole.ADMIN)){
            if(state==DeliveryState.NONE){
                entities = deliveryRepository.findAll();
            } else {
                entities = deliveryRepository.findAllByDeliveryState(state);
            }
        }else {
            if(state==DeliveryState.NONE){
                entities = deliveryRepository.findAllByDealer(user);
            } else {
                entities = deliveryRepository.findAllByDeliveryStateAndDealer(state,user);
            }
        }
        for (DeliveryEntity delivery : entities) {
            SaleEntity sale = delivery.getSale();
            UserEntity buyer = delivery.getSale().getUser();

            BigDecimal total = BigDecimal.ZERO;
            List<DeliveryDetailDto> detailDtos = new ArrayList<>();
            for (SaleDetailEntity detail : sale.getDetails()) {
                Long imgId = 1L;
                for (SectionEntity s : detail.getPublication().getSections()) {
                    if (s.getType() == SecType.PHOTO) {
                        imgId = s.getId();
                        break;
                    }
                }
                UserEntity seller = detail.getPublication().getUser();

                detailDtos.add(new DeliveryDetailDto(
                        detail.getPublication().getId(),
                        detail.getPublication().getName(),
                        url + "/api/image/pub/" + imgId,
                        detail.getTotal(),
                        detail.getCount(),
                        seller.getName() + " " + seller.getLastname(),
                        seller.getPhone(),
                        seller.getState().getName() + ", " + seller.getDirection()
                ));
                total = total.add(detail.getTotal());
            }
            String datetime="";
            String dealerName="";
            if(delivery.getDateTime()!=null) {
                datetime=delivery.getDateTime().toString();
            }
            if(delivery.getDealer()!=null) {
                dealerName=delivery.getDealer().getName() + " " + delivery.getDealer().getLastname();
            }
            list.add(new DeliveryDto(delivery.getId(),
                            sale.getDateTime().toString(),
                            detailDtos,
                            total,
                            buyer.getName() + " " + buyer.getLastname(),
                            buyer.getPhone(),
                            buyer.getState().getName() + ", " + buyer.getDirection(),
                            datetime,
                            delivery.getDeliveryState(),
                            dealerName,
                            delivery.getDealer().getId()
                    )
            );
        }

        if(state.equals(DeliveryState.PENDIENTE)){
            list.sort(Comparator.comparing(DeliveryDto::getSaleDateTime));
        }else {
            list.sort(Comparator.comparing(DeliveryDto::getSaleDateTime).reversed());
        }

        return list;

    }
    DeliveryDto getDeliveryDto(DeliveryEntity delivery) {
        SaleEntity sale = delivery.getSale();
        UserEntity buyer = delivery.getSale().getUser();

        BigDecimal total = BigDecimal.ZERO;
        List<DeliveryDetailDto> detailDtos = new ArrayList<>();
        for (SaleDetailEntity detail : sale.getDetails()) {
            Long imgId = 1L;
            for (SectionEntity s : detail.getPublication().getSections()) {
                if (s.getType() == SecType.PHOTO) {
                    imgId = s.getId();
                    break;
                }
            }
            UserEntity seller = detail.getPublication().getUser();

            detailDtos.add(new DeliveryDetailDto(
                    detail.getPublication().getId(),
                    detail.getPublication().getName(),
                    url + "/api/image/pub/" + imgId,
                    detail.getTotal(),
                    detail.getCount(),
                    seller.getName() + " " + seller.getLastname(),
                    seller.getPhone(),
                    seller.getState().getName() + ", " + seller.getDirection()
            ));
            total = total.add(detail.getTotal());
        }

        return new DeliveryDto(delivery.getId(),
                sale.getDateTime().toString(),
                detailDtos,
                total,
                buyer.getName() + " " + buyer.getLastname(),
                buyer.getPhone(),
                buyer.getState().getName() + ", " + buyer.getDirection(),
                delivery.getDateTime().toString(),
                delivery.getDeliveryState(),
                delivery.getDealer().getName() + " " + delivery.getDealer().getLastname(),
                delivery.getDealer().getId()
        );
    }
    public List<SellDto> getSells(String firstDate, String lastDate, String buyerName, UserEntity user) {
        List<SellDto> list = new ArrayList<>();
        for (SaleDetailEntity detail : saleDetailRepository.findAllBySale_DateTimeBetweenAndPublication_User(
                LocalDateTime.parse(firstDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                LocalDateTime.parse(lastDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                user
        )) {
            SaleEntity sale = detail.getSale();
            UserEntity buyer = sale.getUser();
            if (!buyerName.isBlank() &&
                    !buyer.getName().toLowerCase().contains(buyerName.toLowerCase()) &&
                    !buyer.getLastname().toLowerCase().contains(buyerName.toLowerCase()) &&
                    !buyer.getUsername().toLowerCase().contains(buyerName.toLowerCase()))
            {
                continue;
            }

            Long imgId = 1L;
            for (SectionEntity s : detail.getPublication().getSections()) {
                if (s.getType() == SecType.PHOTO) {
                    imgId = s.getId();
                    break;
                }
            }

            list.add(new SellDto(
                    sale.getId(),
                    sale.getDateTime().toString(),
                    sale.getSaleState(),
                    detail.getPublication().getId(),
                    buyer.getName() +
                            " " +
                            buyer.getLastname(),
                    detail.getPublication().getName(),
                    url + "/api/image/pub/" + imgId,
                    detail.getTotal(),
                    detail.getCount()
            ));

        }
        list.sort(Comparator.comparing(SellDto::getDateTime).reversed());
        return list;
    }

    //Put Delete
    public DeliveryDto putDelivery(PutDeliveryRequest request) {

        DeliveryEntity delivery = deliveryRepository.getReferenceById(request.getId());
        UserEntity dealer = userRepository.getReferenceById(request.getDealer());

        delivery.setDeliveryState(request.getDeliveryState());
        if(delivery.getDeliveryState().equals(DeliveryState.ENTREGADO)){
            delivery.setDateTime(LocalDateTime.now());
        }
        delivery.setDealer(dealer);

        deliveryRepository.save(delivery);

        return getDeliveryDto(delivery);
    }
    public boolean deleteSell(Long id, UserEntity user) {

        SaleEntity sell = saleRepository.getReferenceById(id);
//        if(!user.getId().equals(sell.getUser().getId())) return false;

        sell.setSaleState(SaleState.CANCELADA);

        saleRepository.save(sell);

        return true;
    }
}
