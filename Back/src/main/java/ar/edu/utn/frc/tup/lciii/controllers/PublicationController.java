package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.*;
import ar.edu.utn.frc.tup.lciii.entities.PublicationEntity;
import ar.edu.utn.frc.tup.lciii.services.PublicationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/list")
    public List<PublicationMinDto> getAll() {
        return publicationService.getAll();
    }
    @PostMapping("/list")
    public List<PublicationMinDto> getAll(@RequestBody List<FilterDTO> filterDTOList,
                                          @RequestParam int page, @RequestParam int size) {
        return publicationService.getAllFilthered(filterDTOList,page,size);
    }

    @GetMapping("/{id}")
    public PublicationDto get(@PathVariable Long id) throws EntityNotFoundException {
        return publicationService.get(id);
    }
}
