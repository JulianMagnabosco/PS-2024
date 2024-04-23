package ar.edu.utn.frc.tup.lciii.configs;

import com.github.alexdlaird.ngrok.NgrokClient;
import com.github.alexdlaird.ngrok.protocol.CreateTunnel;
import com.github.alexdlaird.ngrok.protocol.Tunnel;
import com.mercadopago.MercadoPagoConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MPConfig {
    @Bean
    public Tunnel ngrokForMP(){

        MercadoPagoConfig.setAccessToken(
                "APP_USR-832032608255661-031615-ea4f942ba202654d4b337658c5a8e167-1731532938");

        final NgrokClient ngrokClient = new NgrokClient.Builder().build();
        final CreateTunnel createTunnel = new CreateTunnel.Builder()
                .withAddr(8080)
                .build();
        // Open a HTTP tunnel on port 8080
        // <Tunnel: "http://<public_sub>.ngrok.io" -> "http://localhost:8080">
        final Tunnel httpTunnel = ngrokClient.connect(createTunnel);
        return httpTunnel;
    }

}
