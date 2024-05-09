package ps.jmagna.dtos.user;

import ps.jmagna.enums.UserRole;
import lombok.Data;

@Data
public class UserRequest {
    String username;
    String password;
    String email;
    UserRole role;
}
