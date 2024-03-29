package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.LoginRequest;
import ar.edu.utn.frc.tup.lciii.dtos.LoginResponce;
import ar.edu.utn.frc.tup.lciii.dtos.NewUserRequest;
import ar.edu.utn.frc.tup.lciii.dtos.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    UserDto register(NewUserRequest request);

    List<UserDto> getAll();

    LoginResponce login(LoginRequest request);
}
