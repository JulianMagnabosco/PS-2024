package ar.edu.utn.frc.tup.lciii.dtos;

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
    String icon;
    String name;
    String lastname;
    Long idState;
    String direction;
    String numberDir;
    String postalNum;
    String floor;
    String room;
}
