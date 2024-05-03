package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.DeliveryDetailDto;
import ar.edu.utn.frc.tup.lciii.dtos.DeliveryDto;
import ar.edu.utn.frc.tup.lciii.dtos.SellDto;
import ar.edu.utn.frc.tup.lciii.dtos.purchase.NotPurchaseResponce;
import ar.edu.utn.frc.tup.lciii.dtos.purchase.PurchaseResponce;
import ar.edu.utn.frc.tup.lciii.dtos.purchase.SaleDetailDto;
import ar.edu.utn.frc.tup.lciii.dtos.purchase.SaleDto;
import ar.edu.utn.frc.tup.lciii.dtos.requests.PurchaseRequest;
import ar.edu.utn.frc.tup.lciii.entities.*;
import ar.edu.utn.frc.tup.lciii.enums.DeliveryState;
import ar.edu.utn.frc.tup.lciii.enums.SaleState;
import ar.edu.utn.frc.tup.lciii.enums.SecType;
import ar.edu.utn.frc.tup.lciii.enums.UserRole;
import ar.edu.utn.frc.tup.lciii.repository.*;
import com.mercadopago.client.merchantorder.MerchantOrderClient;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.*;
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
    String tunnelUrl;
    @Value("${app.url}")
    private String url;

    @Transactional
    public PurchaseResponce registerSale(PurchaseRequest request) throws MPException, MPApiException {

        PublicationEntity publication = publicationRepository.getReferenceById(request.getIdPub());
        UserEntity user = getUserCompleteData(request.getIdUser());
        if (!publication.isCanSold() && publication.getCount() <= 0) {
            throw new EntityNotFoundException("No se puede vender");
        }
//        Long countTotal= publicationRepository.count();
//        for (SaleDetailEntity detail : publication.getSaleDetails()){
//            if(detail.getSale().getSaleState().equals(SaleState.APPROVED)){
//                countTotal-=detail.getCount();
//            };
//        }
//        if(countTotal<request.getCount()){
//            throw new EntityNotFoundException("Cantidad excede el stock actual");
//        }
        if (publication.getCount() < request.getCount()) {
            throw new EntityNotFoundException("Cantidad excede el stock actual");
        }

        PreferenceItemRequest itemRequest =
                PreferenceItemRequest.builder()
                        .id(publication.getId().toString())
                        .title(publication.getName())
                        .description("")
                        .pictureUrl("")
                        .categoryId("Publicacion")
                        .quantity(request.getCount())
                        .currencyId("ARS")
                        .unitPrice(publication.getPrice())
                        .build();
        List<PreferenceItemRequest> items = new ArrayList<>();
        items.add(itemRequest);

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .backUrls(PreferenceBackUrlsRequest.builder()
                        .success("http://localhost:4200/pub/" + itemRequest.getId()).build())
                .notificationUrl(tunnelUrl + "/api/sell/not?user=" + user.getId())
                .items(items)
                .build();


        PreferenceClient client = new PreferenceClient();
//        System.out.println(preferenceRequest.getNotificationUrl());

        Preference preference = client.create(preferenceRequest);
//        try {
//            System.out.println(objectMapper.writeValueAsString(preference));
//        }catch (Exception ex){
//            System.out.println("error map");
//        }

        return new PurchaseResponce(preference);
    }

    UserEntity getUserCompleteData(Long id){
        UserEntity user = userRepository.getReferenceById(id);

        if(user.getName().isBlank() ||
                user.getLastname().isBlank() ||
                user.getPhone().isBlank() ||
                user.getCvu().isBlank() ||
                user.getDni().isBlank() ||
                user.getDniType().isBlank() ||
                user.getState()==null ||
                user.getDirection().isBlank() ||
                user.getNumberDir().isBlank() ||
                user.getPostalNum().isBlank())
        {
            throw new IllegalArgumentException("El usuario no tiene datos completos ");
        }

        return user;
    }
    @Transactional
    public NotPurchaseResponce notificar(LinkedHashMap notification, String userId) {

        NotPurchaseResponce responce;
//        System.out.println(notification.values());
//        System.out.println(notification.keySet());
        try {
            responce = new NotPurchaseResponce(notification.get("resource").toString(), notification.get("topic").toString());
//            System.out.println(objectMapper.writeValueAsString(notification));

            MerchantOrderClient client = new MerchantOrderClient();
            PaymentClient payClient = new PaymentClient();

            URI uri = new URI(responce.getResource());
            String path = uri.getPath();
            String idStr = path.substring(path.lastIndexOf('/') + 1);
            Long id = Long.parseLong(idStr);

            MerchantOrder m = null;
            if (responce.getTopic().equals("merchant_order")) {

                m = client.get(id);
            } else {
                Payment p = payClient.get(id);
                m = client.get(p.getOrder().getId());
            }
//            System.out.println("+Merchandorder: "+m.getId());

            Optional<SaleEntity> optionalSale = saleRepository.findByMerchantOrder(m.getId());

            SaleEntity sale = new SaleEntity();
            if (optionalSale.isEmpty()) {
                sale.setDateTime(LocalDateTime.now());
                Long uId = Long.parseLong(userId);
                sale.setUser(getUserCompleteData(uId));
                sale.setSaleState(SaleState.PENDIENTE);
                sale.setMerchantOrder(m.getId());

                List<SaleDetailEntity> saleDetails = new ArrayList<>();
                for (MerchantOrderItem item : m.getItems()) {
                    PublicationEntity publication =
                            publicationRepository.getReferenceById(Long.parseLong(item.getId()));

                    SaleDetailEntity detail = new SaleDetailEntity(null,
                            sale, publication, item.getUnitPrice(), item.getQuantity());
                    saleDetails.add(detail);

//                    if (!publication.isCanSold() && publication.getCount() <= 0) {
//                        throw new EntityNotFoundException("No se puede vender");
//                    }
//                    if (publication.getCount() < item.getQuantity()) {
//                        throw new EntityNotFoundException("Cantidad excede el stock actual");
//                    }
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
                if (!m.getShipments().isEmpty()) { // The merchant_order has shipments
                    if (m.getShipments().get(0).getStatus().equals("ready_to_ship")) {
                        System.out.println("Totally paid. Print the label and release your item.");
                    }
                } else { // The merchant_order don't has any shipments
                    System.out.println("Totally paid. Release your item.");

                    sale.setSaleState(SaleState.APROBADA);
                    saleRepository.save(sale);
                }
            } else {
                System.out.println("Not paid yet. Do not release your item." + total
                        + "/" + m.getTotalAmount());
            }

        } catch (Exception ex) {
            responce = null;
            System.out.println("error map: " + ex.getMessage());
        }
        return responce;
    }

    UserEntity getDeliveryFree(){
        UserEntity selected = null;
        Long countSelected = 0L;
        for (UserEntity user: userRepository.findAllByRole(UserRole.DELIVERY)){
            Long count = deliveryRepository.countAllByDealer(user);
            if(count < countSelected){
                selected=user;
                countSelected=count;
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

    public List<DeliveryDto> getDeliveriesPending( Long user) {
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

                detailDtos.add(new DeliveryDetailDto(
                        detail.getPublication().getId(),
                        detail.getPublication().getName(),
                        url + "/api/image/pub/" + imgId,
                        detail.getTotal(),
                        detail.getCount(),
                        buyer.getName()+" "+buyer.getLastname(),
                        buyer.getPhone(),
                        buyer.getState().getName()+", "+buyer.getDirection()
                ));
                total = total.add(detail.getTotal());
            }

            list.add(new DeliveryDto(delivery.getId(),
                    sale.getDateTime().toString(),
                    detailDtos,
                    total,
                    buyer.getName()+" "+buyer.getLastname(),
                   buyer.getPhone(),
                    buyer.getState().getName()+", "+buyer.getDirection(),
                    delivery.getDateTime().toString(),
                    delivery.getDeliveryState(),
                    delivery.getDealer().getName()+" "+delivery.getDealer().getLastname(),
                    delivery.getDealer().getId()
                    )
            );
        }

        return list;

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
}
