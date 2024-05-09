package ps.jmagna.dtos.user;

import ps.jmagna.enums.UserRole;
import lombok.Data;

@Data
public class UserMinDto {
    Long id;
    String username;
    String email;
    UserRole role;
    String iconUrl;

    String name;
    String lastname;

    int pubs;
    int sells;
    int buys;
}
