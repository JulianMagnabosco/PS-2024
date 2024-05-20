package ps.jmagna.dtos.user;

import lombok.Data;

@Data
public class PutUserRequest {
    Long id;
    String username;
    boolean changePass;
    String password;
    String email;

    String name;
    String lastname;
    String phone;
    String dni;
    String dniType;

    Long idState;
    String direction;
    String numberDir;
    String postalNum;
    String floor;
    String room;

    String cvu;
}
