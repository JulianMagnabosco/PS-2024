package ps.jmagna.dtos.user;

import lombok.Data;

@Data
public class LoginRequest {
    String username;
    String password;
}
