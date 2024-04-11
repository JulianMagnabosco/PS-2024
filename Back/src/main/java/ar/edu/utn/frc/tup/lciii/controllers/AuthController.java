package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.configs.auth.TokenProvider;
import ar.edu.utn.frc.tup.lciii.dtos.requests.LoginRequest;
import ar.edu.utn.frc.tup.lciii.dtos.LoginResponce;
import ar.edu.utn.frc.tup.lciii.dtos.requests.UserRequest;
import ar.edu.utn.frc.tup.lciii.dtos.UserDto;
import ar.edu.utn.frc.tup.lciii.entities.UserEntity;
import ar.edu.utn.frc.tup.lciii.services.impl.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;


import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthService service;
    @Autowired
    private TokenProvider tokenService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserRequest data){
        service.signUp(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PostMapping("/signin")
    public ResponseEntity<LoginResponce> signIn(@RequestBody LoginRequest data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword());

        var authUser = authenticationManager.authenticate(usernamePassword);

        UserEntity u = (UserEntity) authUser.getPrincipal();
        var accessToken = tokenService.generateAccessToken(u);

        LoginResponce r = new LoginResponce( u.getId(),
                u.getUsername(),
                u.getRole().toString(),
                u.getEmail(),
                "",
                accessToken);
        return ResponseEntity.ok(r);
    }

    @PostMapping("/prelogin")
    public LoginResponce prelogin(@RequestBody LoginRequest request){
        return service.login(request);
    }
}
