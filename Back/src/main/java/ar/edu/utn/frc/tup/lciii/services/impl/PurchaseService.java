package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.CommentDto;
import ar.edu.utn.frc.tup.lciii.dtos.ListCommentsResponce;
import ar.edu.utn.frc.tup.lciii.dtos.purchase.NotPurchaseResponce;
import ar.edu.utn.frc.tup.lciii.dtos.purchase.PurchaseResponce;
import ar.edu.utn.frc.tup.lciii.dtos.purchase.SaleDetailDto;
import ar.edu.utn.frc.tup.lciii.dtos.purchase.SaleDto;
import ar.edu.utn.frc.tup.lciii.dtos.requests.PurchaseRequest;
import ar.edu.utn.frc.tup.lciii.entities.CommentEntity;
import ar.edu.utn.frc.tup.lciii.entities.PublicationEntity;
import ar.edu.utn.frc.tup.lciii.entities.SaleDetailEntity;
import ar.edu.utn.frc.tup.lciii.entities.SaleEntity;
import ar.edu.utn.frc.tup.lciii.enums.SaleState;
import ar.edu.utn.frc.tup.lciii.repository.PublicationRepository;
import ar.edu.utn.frc.tup.lciii.repository.SaleRepository;
import com.github.alexdlaird.ngrok.protocol.Tunnel;
import com.mercadopago.client.merchantorder.MerchantOrderClient;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
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
    PublicationRepository publicationRepository;

    @Autowired
    Tunnel tunnel;

    @Transactional
    public PurchaseResponce registerSale(PurchaseRequest request) throws MPException, MPApiException {

        PublicationEntity publication = publicationRepository.getReferenceById(request.getIdPub());
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
                        .success("http://localhost:4200/pub/"+itemRequest.getId()).build())
                .notificationUrl(tunnel.getPublicUrl() + "/api/sell/not")
                .items(items)
                .build();

        PreferenceClient client = new PreferenceClient();

        Preference preference = client.create(preferenceRequest);
//        try {
//            System.out.println(objectMapper.writeValueAsString(preference));
//        }catch (Exception ex){
//            System.out.println("error map");
//        }

        return new PurchaseResponce(preference);
    }

    @Transactional
    public NotPurchaseResponce notificar(LinkedHashMap notification) {

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
            System.out.println("+Merchandorder: "+m.getId());

            Optional<SaleEntity> optionalSale = saleRepository.findByMerchantOrder(m.getId());

            SaleEntity sale = new SaleEntity();
            if(optionalSale.isEmpty()){
                List<SaleDetailEntity> saleDetails = new ArrayList<>();
                for (MerchantOrderItem item :m.getItems()){
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
                sale.setSaleState(SaleState.PENDING);
                sale.setMerchantOrder(m.getId());

                saleRepository.saveAndFlush(sale);
            }else {
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

                    sale.setSaleState(SaleState.APPROVED);
                    saleRepository.save(sale);
                }
            } else {
                System.out.println("Not paid yet. Do not release your item." + total
                        + "/" + m.getTotalAmount());
            }

        } catch (Exception ex) {
            responce = null;
            System.out.println("error map");
        }
        return responce;
    }

    public List<SaleDto> getAll() {
        List<SaleDto> list = new ArrayList<>();
        for (SaleEntity sale : saleRepository.findAll()) {

            List<SaleDetailDto> detailDtos = new ArrayList<>();
            for (SaleDetailEntity detail : sale.getDetails()) {
                detailDtos.add(new SaleDetailDto(
                        detail.getPublication().getId(),
                        detail.getTotal(),
                        detail.getCount()
                ));
            }

            list.add(new SaleDto(sale.getId(),
                    detailDtos,
                    sale.getSaleState()));
        }

        return list;

    }
}
