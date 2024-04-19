package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.ListUsersResponce;
import ar.edu.utn.frc.tup.lciii.dtos.stadistics.StatSeriesDto;
import ar.edu.utn.frc.tup.lciii.dtos.stadistics.StatsResponce;
import ar.edu.utn.frc.tup.lciii.services.impl.AuthService;
import ar.edu.utn.frc.tup.lciii.services.impl.StadisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/stats")
public class StadisticsController {
    @Autowired
    private StadisticsService service;
    @GetMapping("/users/{year}")
    public StatsResponce getAll(@PathVariable int year) {
        return service.getUserStadistics(year);
    }
}
