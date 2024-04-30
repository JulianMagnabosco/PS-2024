package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.SellDto;
import ar.edu.utn.frc.tup.lciii.dtos.purchase.NotPurchaseResponce;
import ar.edu.utn.frc.tup.lciii.dtos.purchase.PurchaseResponce;
import ar.edu.utn.frc.tup.lciii.dtos.purchase.SaleDetailDto;
import ar.edu.utn.frc.tup.lciii.dtos.purchase.SaleDto;
import ar.edu.utn.frc.tup.lciii.dtos.requests.PurchaseRequest;
import ar.edu.utn.frc.tup.lciii.entities.*;
import ar.edu.utn.frc.tup.lciii.enums.SaleState;
import ar.edu.utn.frc.tup.lciii.enums.TypeSec;
import ar.edu.utn.frc.tup.lciii.repository.PublicationRepository;
import ar.edu.utn.frc.tup.lciii.repository.SaleDetailRepository;
import ar.edu.utn.frc.tup.lciii.repository.SaleRepository;
import ar.edu.utn.frc.tup.lciii.repository.UserRepository;
import com.github.alexdlaird.ngrok.protocol.Tunnel;
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
    PublicationRepository publicationRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    Tunnel tunnel;
    @Value("${app.url}")
    private String url;

    @Transactional
    public PurchaseResponce registerSale(PurchaseRequest request) throws MPException, MPApiException {

        PublicationEntity publication = publicationRepository.getReferenceById(request.getIdPub());
        UserEntity user = userRepository.getReferenceById(request.getIdUser());
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
                .notificationUrl(tunnel.getPublicUrl() + "/api/sell/not?user=" + user.getId())
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
                sale.setDateTime(LocalDateTime.now());
                Long uId = Long.parseLong(userId);
                sale.setUser(userRepository.getReferenceById(uId));
                sale.setSaleState(SaleState.PENDIENTE);
                sale.setMerchantOrder(m.getId());

                saleRepository.saveAndFlush(sale);
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

    public List<SaleDto> getPurchases(String firstDate, String lastDate, Long user) {
        List<SaleDto> list = new ArrayList<>();
        for (SaleEntity sale : saleRepository.findAllByDateTimeBetween(
                LocalDateTime.parse(firstDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                LocalDateTime.parse(lastDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
        )) {
            if (!user.equals(sale.getUser().getId())) {
                continue;
            }
            BigDecimal total = BigDecimal.ZERO;
            List<SaleDetailDto> detailDtos = new ArrayList<>();
            for (SaleDetailEntity detail : sale.getDetails()) {
                Long imgId = 1L;
                for (SectionEntity s : detail.getPublication().getSections()) {
                    if (s.getType() == TypeSec.PHOTO) {
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


    public List<SellDto> getSells(String firstDate, String lastDate, Long user) {
        List<SellDto> list = new ArrayList<>();
        for (SaleDetailEntity detail : saleDetailRepository.findAllBySale_DateTimeBetweenAndPublication_User_Id(
                LocalDateTime.parse(firstDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                LocalDateTime.parse(lastDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                user
        )) {
            Long imgId = 1L;
            for (SectionEntity s : detail.getPublication().getSections()) {
                if (s.getType() == TypeSec.PHOTO) {
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
