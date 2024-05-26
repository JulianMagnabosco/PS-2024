package ps.jmagna.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import ps.jmagna.dtos.user.ListUsersResponce;
import ps.jmagna.dtos.user.UserDto;
import ps.jmagna.enums.UserRole;
import ps.jmagna.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private AuthService service;
    @GetMapping("/list")
    public ListUsersResponce getAll(@RequestParam(required = false,defaultValue = "") String text,
                                    @RequestParam(required = false,defaultValue = "0") int page,
                                    @RequestParam(required = false,defaultValue = "5") int size) {
        return service.getAll(text,page,size);
    }
    @GetMapping("/dealers")
    public ListUsersResponce getDealers() {
        return service.getDealers();
    }
    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id) {
        return service.get(id);
    }


    @PutMapping("/role")
    public UserDto get(@RequestParam Long id,
                       @RequestParam UserRole role,
                       @AuthenticationPrincipal Jwt authentication) {
        return service.putRole(id, role, authentication);
    }
//    @PutMapping("/mod")
//    public UserDto getImage(@RequestBody PutUserRequest request) {
//        return service.put(request);
//    }
    @PutMapping(value = "/mod", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserDto put(@RequestParam(value = "icon",required = false) MultipartFile icon,
                       @RequestParam("data") String data,
                       @AuthenticationPrincipal Jwt authentication) throws IOException {
        return service.put(data,icon,authentication);
    }
}
