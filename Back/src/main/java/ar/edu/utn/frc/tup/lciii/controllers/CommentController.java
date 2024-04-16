package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.CommentDto;
import ar.edu.utn.frc.tup.lciii.dtos.ListCommentsResponce;
import ar.edu.utn.frc.tup.lciii.dtos.PublicationDto;
import ar.edu.utn.frc.tup.lciii.dtos.requests.CommentRequest;
import ar.edu.utn.frc.tup.lciii.dtos.requests.PublicationRequest;
import ar.edu.utn.frc.tup.lciii.services.PublicationService;
import ar.edu.utn.frc.tup.lciii.services.impl.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
}
