package ar.edu.utn.frc.tup.lciii.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponce {
    Long id;
    String username;
    String role;
    String email;
    String iconUrl;
    String token;

    public LoginResponce(String token){
        this.token = token;
    }
}
