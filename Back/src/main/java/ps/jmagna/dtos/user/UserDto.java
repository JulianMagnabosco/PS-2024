package ps.jmagna.dtos.user;

import ps.jmagna.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {
    Long id;
    String username;
    String email;
    UserRole role;
    String iconUrl;

    String name;
    String lastname;
    String phone;
    String dni;
    String dniType;

    Long idState;
    String state;
    String direction;
    String numberDir;
    String postalNum;
    String floor;
    String room;

    String mpClient;
    String mpSecret;
}
