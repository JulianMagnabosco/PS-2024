package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.*;
import ar.edu.utn.frc.tup.lciii.dtos.requests.PublicationRequest;
import ar.edu.utn.frc.tup.lciii.dtos.requests.SearchRequest;
import ar.edu.utn.frc.tup.lciii.entities.PublicationEntity;
import ar.edu.utn.frc.tup.lciii.services.PublicationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public boolean postImage(@RequestParam("images") MultipartFile[] images,
                                       @RequestParam("pub")String pub,
                                       @RequestParam("indexes") String indexes) throws IOException {
        return publicationService.registerImg(images,pub,indexes);
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


    @GetMapping(value="/image", produces = {MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_GIF_VALUE})
    public byte[] getImage(@RequestParam("pub")String pub,
            @RequestParam("index") String index) throws EntityNotFoundException {
        return publicationService.getImage(pub, index);
    }
}
