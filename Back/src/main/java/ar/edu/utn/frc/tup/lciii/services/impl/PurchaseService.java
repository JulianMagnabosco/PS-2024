package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.purchase.NotPurchaseResponce;
import ar.edu.utn.frc.tup.lciii.dtos.purchase.PurchaseResponce;
import ar.edu.utn.frc.tup.lciii.dtos.requests.PurchaseRequest;
import com.github.alexdlaird.ngrok.protocol.Tunnel;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.merchantorder.MerchantOrderClient;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.merchantorder.MerchantOrder;
import com.mercadopago.resources.merchantorder.MerchantOrderPayment;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class PurchaseService {


    @Autowired
    Tunnel tunnel;

    public PurchaseResponce registrar (PurchaseRequest request) throws MPException, MPApiException {



        PreferenceItemRequest itemRequest =
                PreferenceItemRequest.builder()
                        .id("1234")
                        .title("Games")
                        .description("PS5")
                        .pictureUrl("http://picture.com/PS5")
                        .categoryId("games")
                        .quantity(1)
                        .currencyId("ARS")
                        .unitPrice(new BigDecimal("10"))
                        .build();
        List<PreferenceItemRequest> items = new ArrayList<>();
        items.add(itemRequest);

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .notificationUrl(tunnel.getPublicUrl()+"/not")
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

    public NotPurchaseResponce notificar(LinkedHashMap notification){

        NotPurchaseResponce restopic;
        try {
            restopic = new NotPurchaseResponce(notification.get("resource").toString(),notification.get("topic").toString());
//            System.out.println(objectMapper.writeValueAsString(notification));

            MerchantOrderClient client = new MerchantOrderClient();
            PaymentClient payClient = new PaymentClient();

            URI uri = new URI(restopic.getResource());
            String path = uri.getPath();
            String idStr = path.substring(path.lastIndexOf('/') + 1);
            Long id = Long.parseLong(idStr);

            MerchantOrder m = null;
            if(restopic.getTopic().equals("merchant_order")){

                m =client.get(id);
//                System.out.println(objectMapper.writeValueAsString(m));
            }else {
                Payment p = payClient.get(id);
                m =client.get(p.getOrder().getId());
//                System.out.println(objectMapper.writeValueAsString(m));
            }

            BigDecimal total=BigDecimal.ZERO;
            for(MerchantOrderPayment mpay :m.getPayments()){
                if(mpay.getStatus().equals("approved")){
                    total=total.add(mpay.getTotalPaidAmount());
                }
            }

            if( total.compareTo(m.getTotalAmount()) >=0){
                if (!m.getShipments().isEmpty()) { // The merchant_order has shipments
                    if(m.getShipments().get(0).getStatus().equals("ready_to_ship")) {
                        System.out.println("Totally paid. Print the label and release your item.");
                    }
                } else { // The merchant_order don't has any shipments
                    System.out.println("Totally paid. Release your item.");
                }
            } else {
                System.out.println("Not paid yet. Do not release your item."+total
                        +"/"+m.getTotalAmount());
            }

        }catch (Exception ex){
            restopic=null;
            System.out.println("error map");
        }
        return restopic;
    }
}