package ar.edu.utn.frc.tup.lciii.dtos.requests;

import lombok.Data;

@Data
public class LoginRequest {
    String username;
    String password;
}
