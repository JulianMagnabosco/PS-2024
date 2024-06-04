package ps.jmagna.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import ps.jmagna.dtos.publication.*;
import ps.jmagna.services.AuthService;
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
    @Autowired
    AuthService authService;

    @PostMapping("/new")
    public PublicationDto post(@RequestBody PublicationRequest request,
                               @AuthenticationPrincipal Jwt authentication) {
        return publicationService.register(request,authService.findUser(authentication));
    }

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public boolean postImage(@RequestParam("images") MultipartFile[] images,
                             @RequestParam("indexes") String indexes,
                             @AuthenticationPrincipal Jwt authentication) throws IOException {
        return publicationService.registerImg(images,indexes,authService.findUser(authentication));
    }
    @PostMapping("/cal")
    public boolean calificate(@RequestBody CalificationRequest request,
                              @AuthenticationPrincipal Jwt authentication) {
        return publicationService.calificate(request,authService.findUser(authentication));
    }
    @PostMapping("/cart")
    public boolean addCart(@RequestBody CalificationRequest request,
                           @AuthenticationPrincipal Jwt authentication) {
        return publicationService.addCart(request,authService.findUser(authentication));
    }

    //Busqueda
    @PostMapping("/search")
    public SearchPubResponce getAll(@RequestBody SearchPubRequest searchPubRequest,
                                    @AuthenticationPrincipal Jwt authentication) {
        return publicationService.getAll(searchPubRequest, authService.findUser(authentication));
    }
    @GetMapping("/sugg")
    public List<String> getSuggestions(@RequestParam String text) {
        return publicationService.getSuggestions(text);
    }
    @GetMapping("/cart")
    public List<CartDto> getCart(@AuthenticationPrincipal Jwt authentication) {
        return publicationService.getCart(authService.findUser(authentication));
    }
    @GetMapping("/drafts")
    public List<PublicationMinDto> getDrafts(@AuthenticationPrincipal Jwt authentication) {
        return publicationService.getDrafts(authService.findUser(authentication));
    }

    @GetMapping("/{id}")
    public PublicationDto get(@PathVariable Long id,
                              @AuthenticationPrincipal Jwt authentication) throws EntityNotFoundException {
        return publicationService.get(id,authService.findUser(authentication));
    }

    //Put
    @PutMapping("/mod")
    public PublicationDto put(@RequestBody PutPublicationRequest request,
                              @AuthenticationPrincipal Jwt authentication) {
        return publicationService.put(request,authService.findUser(authentication));
    }

    //Delete
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id,
                          @AuthenticationPrincipal Jwt authentication) throws EntityNotFoundException {
        return publicationService.delete(id,authService.findUser(authentication));
    }
}
