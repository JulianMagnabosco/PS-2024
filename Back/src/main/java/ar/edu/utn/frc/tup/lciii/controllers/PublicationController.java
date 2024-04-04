package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.*;
import ar.edu.utn.frc.tup.lciii.dtos.requests.PublicationRequest;
import ar.edu.utn.frc.tup.lciii.dtos.requests.SearchRequest;
import ar.edu.utn.frc.tup.lciii.entities.PublicationEntity;
import ar.edu.utn.frc.tup.lciii.services.PublicationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pub")
public class PublicationController {
    @Autowired
    PublicationService publicationService;

    @PostMapping("/new")
    public PublicationEntity post(@RequestBody PublicationRequest request) {
        return publicationService.register(request);
    }

    @GetMapping("/list")
    public List<PublicationMinDto> getAll() {
        return publicationService.getAll();
    }
    @PostMapping("/search")
    public SearchResponce getAll(@RequestBody SearchRequest searchRequest) {
        return publicationService.getAllFilthered(searchRequest);
    }

    @GetMapping("/{id}")
    public PublicationDto get(@PathVariable Long id) throws EntityNotFoundException {
        return publicationService.get(id);
    }
}
