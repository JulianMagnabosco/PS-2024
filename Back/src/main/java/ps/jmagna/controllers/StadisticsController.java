package ps.jmagna.controllers;

import ps.jmagna.dtos.stadistics.StatsResponce;
import ps.jmagna.services.StadisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/pubs/{year}/{month}")
    public StatsResponce getAll(@PathVariable int year,@PathVariable int month) {
        return service.getPublicationStadistics(year,month);
    }
    @GetMapping("/sells")
    public StatsResponce getAll(@RequestParam String type,@RequestParam String date1,@RequestParam String date2) {
        return service.getSellsStadistics(type,date1,date2);
    }
}
