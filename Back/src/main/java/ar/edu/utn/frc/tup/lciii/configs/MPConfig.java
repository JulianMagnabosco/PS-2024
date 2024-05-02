package ar.edu.utn.frc.tup.lciii.configs;

import com.mercadopago.MercadoPagoConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Configuration
public class MPConfig {
    @Bean
    public String ngrokForMP(){

        MercadoPagoConfig.setAccessToken(
                "APP_USR-832032608255661-031615-ea4f942ba202654d4b337658c5a8e167-1731532938");

//        try {
//            // Ejecutar el comando de Ngrok
//            String projectDirectoryPath = System.getProperty("user.dir").replace("\\Back","");
//            ProcessBuilder builder = new ProcessBuilder(projectDirectoryPath+"\\ngrok.exe", "http", "8080");
//            builder.redirectErrorStream(true);
//
//            // Crea un objeto File con la ruta del directorio del proyecto
//            File dir = new File(projectDirectoryPath);
//            System.out.println(dir);
//            builder.directory(dir);
//            Process process = builder.start();
//
//            // Leer la salida del proceso
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//                // Buscar la línea que contiene la URL generada
//                if (line.contains("Forwarding")) {
//                    Tunnel tunnel= new Tunnel();
//                    tunnel.setPublicUrl(line.split(" ")[1]);
//                    return tunnel;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;

//        final NgrokClient ngrokClient = new NgrokClient.Builder().build();
//        final CreateTunnel createTunnel = new CreateTunnel.Builder()
//                .withAddr(8080)
//                .build();
//        // Open a HTTP tunnel on port 8080
//        // <Tunnel: "http://<public_sub>.ngrok.io" -> "http://localhost:8080">
//        final Tunnel httpTunnel = ngrokClient.connect(createTunnel);
        RestTemplate restTemplate = new RestTemplate();

        // Hacer una solicitud GET y recibir la respuesta en una variable
        String responseData = restTemplate.getForObject("http://localhost:4040/api/tunnels", String.class);

        String httpTunnel = "";
        if (responseData != null) {
            httpTunnel = responseData.substring(
                    responseData.indexOf(":",responseData.indexOf("public_url"))+2,
                    responseData.indexOf(",",responseData.indexOf("public_url"))-1
            );
        }
        System.out.println(httpTunnel);
        return httpTunnel;
    }

}
