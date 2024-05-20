package ps.jmagna.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import ps.jmagna.dtos.publication.CommentDto;
import ps.jmagna.dtos.publication.ListCommentsResponce;
import ps.jmagna.dtos.publication.CommentRequest;
import ps.jmagna.services.AuthService;
import ps.jmagna.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comm")
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    AuthService authService;

    @PostMapping("/new")
    public CommentDto post(@RequestBody CommentRequest request,
                           @AuthenticationPrincipal Jwt authentication) {
        return commentService.register(request,authService.findUser(authentication));
    }

    @GetMapping(value = "/list/{id}")
    public ListCommentsResponce getAll(@PathVariable Long id) {
        return commentService.getAll(id);
    }
    @DeleteMapping(value = "/del/{id}")
    public boolean delete(@PathVariable Long id,
                          @AuthenticationPrincipal Jwt authentication) {
        return commentService.delete(id,authService.findUser(authentication));
    }
}
