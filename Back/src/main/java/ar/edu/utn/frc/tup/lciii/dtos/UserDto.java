package ar.edu.utn.frc.tup.lciii.dtos;

import ar.edu.utn.frc.tup.lciii.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
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
    String cvu;
    String dni;
    String dniType;

    Long idState;
    String state;
    String direction;
    String numberDir;
    String postalNum;
    String floor;
    String room;
}
