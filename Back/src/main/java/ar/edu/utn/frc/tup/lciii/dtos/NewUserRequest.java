package ar.edu.utn.frc.tup.lciii.dtos;

import ar.edu.utn.frc.tup.lciii.enums.UserRole;
import lombok.Data;

@Data
public class NewUserRequest {
    String name;
    String password;
    String email;
    UserRole role;
}
