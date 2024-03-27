package ar.edu.utn.frc.tup.lciii.dtos;

import lombok.Data;

@Data
public class LoginRequest {
    String name;
    String password;
}
