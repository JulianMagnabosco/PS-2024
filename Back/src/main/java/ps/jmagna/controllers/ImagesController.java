package ps.jmagna.controllers;

import ps.jmagna.services.AuthService;
import ps.jmagna.services.PublicationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/image")
public class ImagesController {
    @Autowired
    PublicationService publicationService;
    @Autowired
    AuthService userService;
    @GetMapping(value = "/user/{id}",produces = {MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_GIF_VALUE})
    public byte[] getImageUser(@PathVariable Long id) {
        return userService.getImage(id);
    }

    @GetMapping(value = "/pub/{id}", produces = {MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,MediaType.IMAGE_GIF_VALUE})
    public byte[] getImagePub(@PathVariable Long id) throws EntityNotFoundException {
        return publicationService.getImage(id);
    }
}
