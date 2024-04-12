package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.ListUsersResponce;
import ar.edu.utn.frc.tup.lciii.dtos.SearchPubResponce;
import ar.edu.utn.frc.tup.lciii.dtos.UserDto;
import ar.edu.utn.frc.tup.lciii.dtos.requests.PutUserRequest;
import ar.edu.utn.frc.tup.lciii.services.impl.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private AuthService service;
    @GetMapping("/list")
    public ListUsersResponce getAll(@RequestParam(required = false,defaultValue = "") String text,
                                    @RequestParam(required = false,defaultValue = "0") int page,
                                    @RequestParam(required = false,defaultValue = "5") int size) {
        return service.getAll(text,page,size);
    }
    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id) {
        return service.get(id);
    }
    @GetMapping(value = "/image/{id}",produces = {MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_GIF_VALUE})
    public byte[] getImage(@PathVariable Long id) {
        return service.getImage(id);
    }
//    @PutMapping("/mod")
//    public UserDto getImage(@RequestBody PutUserRequest request) {
//        return service.put(request);
//    }
    @PutMapping(value = "/mod", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserDto put(@RequestParam("icon") MultipartFile[] icon,
                             @RequestParam("data") String data) throws IOException {
        return service.put(data,icon[0]);
    }
}
