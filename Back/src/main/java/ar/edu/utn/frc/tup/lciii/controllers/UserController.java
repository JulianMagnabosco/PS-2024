package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.configs.JwtUtil;
import ar.edu.utn.frc.tup.lciii.dtos.LoginRequest;
import ar.edu.utn.frc.tup.lciii.dtos.LoginResponce;
import ar.edu.utn.frc.tup.lciii.dtos.NewUserRequest;
import ar.edu.utn.frc.tup.lciii.dtos.UserDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.ErrorApi;
import ar.edu.utn.frc.tup.lciii.entities.UserEntity;
import ar.edu.utn.frc.tup.lciii.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/rest/auth")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/olduser")
    public UserDto post(@RequestBody NewUserRequest request) {
        return userService.register(request);
    }
    @GetMapping("/oldusers")
    public List<UserDto> getAll() {
        return userService.getAll();
    }
    @PostMapping("/oldlogin")
    public LoginResponce oldlogin(@RequestBody LoginRequest request){
        return userService.login(request);
    }

    private final AuthenticationManager authenticationManager;

    private JwtUtil jwtUtil;
    public UserController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody LoginRequest loginReq)  {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getName(), loginReq.getPassword()));
            String name = authentication.getName();
            UserEntity user = new UserEntity();
            user.setName(loginReq.getName());
            user.setPassword("");
            String token = jwtUtil.createToken(user);
            LoginResponce loginRes = new LoginResponce(name,token);

            return ResponseEntity.ok(loginRes);

        }catch (BadCredentialsException e){
            ErrorApi errorResponse = new ErrorApi("",HttpStatus.BAD_REQUEST.ordinal(),"","Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }catch (Exception e){
            ErrorApi errorResponse = new ErrorApi("",HttpStatus.BAD_REQUEST.ordinal(),"",e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
