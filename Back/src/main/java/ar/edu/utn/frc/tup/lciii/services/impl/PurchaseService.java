package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.clients.CustomMPClient;
import ar.edu.utn.frc.tup.lciii.dtos.DeliveryDetailDto;
import ar.edu.utn.frc.tup.lciii.dtos.DeliveryDto;
import ar.edu.utn.frc.tup.lciii.dtos.SellDto;
import ar.edu.utn.frc.tup.lciii.dtos.purchase.NotificationResponce;
import ar.edu.utn.frc.tup.lciii.dtos.purchase.PurchaseResponce;
import ar.edu.utn.frc.tup.lciii.dtos.purchase.SaleDetailDto;
import ar.edu.utn.frc.tup.lciii.dtos.purchase.SaleDto;
import ar.edu.utn.frc.tup.lciii.dtos.requests.PurchaseItemRequest;
import ar.edu.utn.frc.tup.lciii.dtos.requests.PurchaseRequest;
import ar.edu.utn.frc.tup.lciii.dtos.requests.PutDeliveryRequest;
import ar.edu.utn.frc.tup.lciii.entities.*;
import ar.edu.utn.frc.tup.lciii.enums.DeliveryState;
import ar.edu.utn.frc.tup.lciii.enums.SaleState;
import ar.edu.utn.frc.tup.lciii.enums.SecType;
import ar.edu.utn.frc.tup.lciii.enums.UserRole;
import ar.edu.utn.frc.tup.lciii.repository.*;
import com.mercadopago.client.merchantorder.MerchantOrderClient;
import com.mercadopago.client.oauth.CreateOauthCredentialRequest;
import com.mercadopago.client.oauth.OauthClient;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static ar.edu.utn.frc.tup.lciii.services.impl.AuthService.userCanBuy;

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

    @Transactional
    public PurchaseResponce registerSale(PurchaseRequest request) throws MPException, MPApiException {

        UserEntity user = userRepository.getReferenceById(request.getIdUser());
        if(userCanBuy(user)){
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
                        .success("http://localhost:4200/purchases").build())
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

        String path =  URI.create(responce.getResource()).getPath();
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

        SaleEntity sale;
        if (optionalSale.isEmpty()) {
            sale = registerSaleDelivery(userId, m);
        } else {
            sale = optionalSale.get();
        }

        BigDecimal total = BigDecimal.ZERO;
        for (MerchantOrderPayment mpay : m.getPayments()) {
            if (mpay.getStatus().equals("approved")) {
                total = total.add(mpay.getTotalPaidAmount());
            }
        }

        if (total.compareTo(m.getTotalAmount()) >= 0) {
            if (m.getShipments().isEmpty()) { // The merchant_order don't has any shipments
                System.out.println("Totally paid. Release your item.");

                PaymentPayerRequest payerRequest = PaymentPayerRequest.builder()
                        .email(emailApp)
                        .build();

                for (SaleDetailEntity detail : sale.getDetails()) {

                    String token = customMPClient.getToken(detail.getPublication().getUser().getMpClient(),
                            detail.getPublication().getUser().getMpSecret());

                    MPRequestOptions options = MPRequestOptions.builder()
                            .accessToken(token)
                            .build();
                    PaymentCreateRequest newPayment = PaymentCreateRequest.builder()
                            .payer(payerRequest)
                            .transactionAmount(detail.getTotal())
                            .description("Pay by "+ detail.getPublication().getName())
                            .build();
                    paymentClient.create(newPayment,options);
                }

                sale.setSaleState(SaleState.APROBADA);
                saleRepository.save(sale);
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
        if(userCanBuy(user)){
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

    UserEntity getDeliveryFree() {
        UserEntity selected = null;
        Long countSelected = 0L;
        for (UserEntity user : userRepository.findAllByRole(UserRole.DELIVERY)) {
            Long count = deliveryRepository.countAllByDealer(user);
            if (count < countSelected) {
                selected = user;
                countSelected = count;
            }
        }

        return selected;
    }

    public List<SaleDto> getPurchases(String firstDate, String lastDate, Long user) {
        List<SaleDto> list = new ArrayList<>();
        for (SaleEntity sale : saleRepository.findAllByDateTimeBetweenAndUser_Id(
                LocalDateTime.parse(firstDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                LocalDateTime.parse(lastDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                user
        )) {
//            if (!user.equals(sale.getUser().getId())) {
//                continue;
//            }
            BigDecimal total = BigDecimal.ZERO;
            List<SaleDetailDto> detailDtos = new ArrayList<>();
            for (SaleDetailEntity detail : sale.getDetails()) {
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

            list.add(new SaleDto(sale.getId(),
                    sale.getDateTime().toString(),
                    detailDtos,
                    sale.getSaleState(),
                    total)
            );
        }

        return list;

    }

    public List<DeliveryDto> getDeliveriesPending(Long user) {
        List<DeliveryDto> list = new ArrayList<>();
        for (DeliveryEntity delivery : deliveryRepository.findAllByDealer_Id(user)) {
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

            list.add(new DeliveryDto(delivery.getId(),
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
                    )
            );
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

    public DeliveryDto putDelivery(PutDeliveryRequest request) {

        DeliveryEntity delivery = deliveryRepository.getReferenceById(request.getId());
        UserEntity dealer = userRepository.getReferenceById(request.getDealer());

        delivery.setDeliveryState(request.getDeliveryState());
        delivery.setDealer(dealer);

        deliveryRepository.save(delivery);

        return getDeliveryDto(delivery);
    }

    public List<SellDto> getSells(String firstDate, String lastDate, Long user) {
        List<SellDto> list = new ArrayList<>();
        for (SaleDetailEntity detail : saleDetailRepository.findAllBySale_DateTimeBetweenAndPublication_User_Id(
                LocalDateTime.parse(firstDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                LocalDateTime.parse(lastDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                user
        )) {
            Long imgId = 1L;
            for (SectionEntity s : detail.getPublication().getSections()) {
                if (s.getType() == SecType.PHOTO) {
                    imgId = s.getId();
                    break;
                }
            }

            list.add(new SellDto(
                    detail.getSale().getId(),
                    detail.getSale().getDateTime().toString(),
                    detail.getSale().getSaleState(),
                    detail.getPublication().getId(),
                    detail.getSale().getUser().getName() +
                            " " +
                            detail.getSale().getUser().getLastname(),
                    detail.getPublication().getName(),
                    url + "/api/image/pub/" + imgId,
                    detail.getTotal(),
                    detail.getCount()
            ));

        }
        return list;
    }
    public boolean deleteSell(Long id) {

        SaleEntity sell = saleRepository.getReferenceById(id);

        sell.setSaleState(SaleState.CANCELADA);

        saleRepository.save(sell);

        return true;
    }
}
