package ps.jmagna.dtos.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import ps.jmagna.enums.UserRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    Long id;
    String username;
    String email;
    UserRole role;
    String iconUrl;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime dateTime;

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

    String cvu;

    boolean same;
}
