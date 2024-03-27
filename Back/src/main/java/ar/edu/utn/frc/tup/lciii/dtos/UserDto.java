package ar.edu.utn.frc.tup.lciii.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    Long Id;
    String Name;
    String Password;
    String Email;
    String Icon;
    Long IdDirection;
    String NumberDir;
}
