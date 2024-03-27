package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.LoginRequest;
import ar.edu.utn.frc.tup.lciii.dtos.LoginResponce;
import ar.edu.utn.frc.tup.lciii.dtos.NewUserRequest;
import ar.edu.utn.frc.tup.lciii.dtos.UserDto;
import ar.edu.utn.frc.tup.lciii.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/user")
    public UserDto post(@RequestBody NewUserRequest request) {
        return userService.register(request);
    }
    @GetMapping("/users")
    public List<UserDto> getAll() {
        return userService.getAll();
    }
    @PostMapping("/login")
    public LoginResponce login(@RequestBody LoginRequest request){
        return userService.login(request);
    }
}
