package ps.jmagna.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import ps.jmagna.dtos.publication.*;
import ps.jmagna.services.PublicationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/api/pub")
public class PublicationController {
    @Autowired
    PublicationService publicationService;

    @PostMapping("/new")
    public PublicationDto post(@RequestBody PublicationRequest request) {
        return publicationService.register(request);
    }

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public boolean postImage(@RequestParam("images") MultipartFile[] images,
                             @RequestParam("indexes") String indexes) throws IOException {
        return publicationService.registerImg(images,indexes);
    }
    @PostMapping("/cal")
    public boolean calificate(@RequestBody CalificationRequest request) {
        return publicationService.calificate(request);
    }
    @PostMapping("/cart")
    public boolean addCart(@RequestBody CalificationRequest request) {
        return publicationService.addCart(request);
    }
    //Busqueda
    @PostMapping("/search")
    public SearchPubResponce getAll(@RequestBody SearchPubRequest searchPubRequest,
                                    @AuthenticationPrincipal Jwt authentication) {
        return publicationService.getAll(searchPubRequest, authentication.getSubject());
    }
    @GetMapping("/cart")
    public List<CartDto> getCart(@AuthenticationPrincipal Jwt authentication) {
        return publicationService.getCart(authentication.getSubject());
    }

    @GetMapping("/{id}/{userId}")
    public PublicationDto get(@PathVariable Long id,@PathVariable Long userId) throws EntityNotFoundException {
        return publicationService.get(id,userId);
    }

    //Put
    @PutMapping("/mod")
    public PublicationDto put(@RequestBody PutPublicationRequest request) {
        return publicationService.put(request);
    }

    //Delete
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) throws EntityNotFoundException {
        return publicationService.delete(id);
    }
}
