package ps.jmagna.configs;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.merchantorder.MerchantOrderClient;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MPConfig {
    @Autowired
    RestTemplate restTemplate;

    @Value("${mp.access-token}")
    String accessToken;
    @Bean
    public String ngrokForMP(){

        MercadoPagoConfig.setAccessToken(accessToken);

        String httpTunnel = "";

        try {
            String responseData = restTemplate.getForObject("http://localhost:4040/api/tunnels", String.class);
            if (responseData != null) {
                httpTunnel = responseData.substring(
                        responseData.indexOf(":",responseData.indexOf("public_url"))+2,
                        responseData.indexOf(",",responseData.indexOf("public_url"))-1
                );
            }
            System.out.println(httpTunnel);
        }catch (Exception e){
            System.out.println("No proxy");
        }
        return httpTunnel;
    }

    @Bean
    PreferenceClient preferenceClient(){
        return new PreferenceClient();
    }
    @Bean
    MerchantOrderClient merchantOrderClient(){
        return new MerchantOrderClient();
    }
    @Bean
    PaymentClient paymentClient(){
        return new PaymentClient();
    }

}
