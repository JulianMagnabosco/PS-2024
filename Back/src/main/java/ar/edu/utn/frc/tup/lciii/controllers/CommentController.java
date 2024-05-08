package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.CommentDto;
import ar.edu.utn.frc.tup.lciii.dtos.ListCommentsResponce;
import ar.edu.utn.frc.tup.lciii.dtos.requests.CommentRequest;
import ar.edu.utn.frc.tup.lciii.services.impl.CommentService;
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
