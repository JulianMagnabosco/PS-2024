package ar.edu.utn.frc.tup.lciii.dtos;

import ar.edu.utn.frc.tup.lciii.enums.UserRole;
import lombok.Data;

@Data
public class UserMinDto {
    Long id;
    String username;
    String email;
    UserRole role;
    String iconUrl;

    String name;
    String lastname;

    boolean canBuy;
    boolean canSell;

    int pubs;
    int sells;
    int buys;
}
