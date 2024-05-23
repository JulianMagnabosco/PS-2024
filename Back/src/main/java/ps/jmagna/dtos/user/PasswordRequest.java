package ps.jmagna.dtos.user;

import lombok.Data;

@Data
public class PasswordRequest {
    String token;
    String email;
    String password;
}
