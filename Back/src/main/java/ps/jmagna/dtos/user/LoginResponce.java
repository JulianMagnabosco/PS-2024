package ps.jmagna.dtos.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ps.jmagna.enums.UserRole;

@Data
public class LoginResponce {

    Long id;
    String username;
    String email;
    UserRole role;
    String iconUrl;

    String name;
    String lastname;

    boolean canBuy;
    boolean canSell;

    String token;
}
