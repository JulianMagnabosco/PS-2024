package ps.jmagna.controllers;

import ps.jmagna.dtos.publication.CommentDto;
import ps.jmagna.dtos.publication.ListCommentsResponce;
import ps.jmagna.dtos.publication.CommentRequest;
import ps.jmagna.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comm")
public class CommentController {
    @Autowired
    CommentService commentService;

    @PostMapping("/new")
    public CommentDto post(@RequestBody CommentRequest request) {
        return commentService.register(request);
    }

    @GetMapping(value = "/list/{id}")
    public ListCommentsResponce getAll(@PathVariable Long id) {
        return commentService.getAll(id);
    }
    @DeleteMapping(value = "/del/{id}")
    public boolean delete(@PathVariable Long id) {
        return commentService.delete(id);
    }
}
