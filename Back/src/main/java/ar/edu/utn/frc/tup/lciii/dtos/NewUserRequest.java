package ar.edu.utn.frc.tup.lciii.dtos;

import lombok.Data;

@Data
public class NewUserRequest {
    String Name;
    String Password;
    String Email;
    String Icon;
    Long IdDirection;
    String NumberDir;
}
