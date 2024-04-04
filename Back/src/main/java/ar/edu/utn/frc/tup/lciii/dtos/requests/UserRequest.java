package ar.edu.utn.frc.tup.lciii.dtos.requests;

import ar.edu.utn.frc.tup.lciii.enums.UserRole;
import lombok.Data;

@Data
public class UserRequest {
    String name;
    String password;
    String email;
    UserRole role;
}
