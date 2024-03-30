package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.LoginRequest;
import ar.edu.utn.frc.tup.lciii.dtos.NewUserRequest;
import ar.edu.utn.frc.tup.lciii.dtos.PublicationRequest;
import ar.edu.utn.frc.tup.lciii.dtos.UserDto;
import ar.edu.utn.frc.tup.lciii.entities.PublicationEntity;
import ar.edu.utn.frc.tup.lciii.services.PublicationService;
import ar.edu.utn.frc.tup.lciii.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/pub")
public class PublicationController {
    @Autowired
    PublicationService publicationService;

    @PostMapping("/new")
    public PublicationEntity post(@RequestBody PublicationRequest request) {
        return publicationService.register(request);
    }

    @PostMapping("/ping")
    public LoginRequest post(@RequestBody LoginRequest request) {
        return request;
    }
}
