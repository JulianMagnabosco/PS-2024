package ps.jmagna.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomMPClient {
    @Autowired
    private RestTemplate restTemplate;
    private String baseResourceUrl = "";

    public String getToken(String client, String secret) {
        String get = restTemplate.postForObject(
                baseResourceUrl,
                new TokenRequest(client,secret,"client_credentials"),
                String.class
        );

        if (get != null) {
            String token = get.substring(
                    get.indexOf(":",get.indexOf("access_token"))+2,
                    get.indexOf(",",get.indexOf("access_token"))-1
            );
            return token;
        }
        return null;
    }


}
