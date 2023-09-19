package com.springboot.personalblog.controller;

import com.springboot.personalblog.DTO.CommentDTO;
import com.springboot.personalblog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comments")
    public ResponseEntity<CommentDTO> createComment(@PathVariable(value = "postId") long postId, @RequestBody CommentDTO commentDTO){
        return new ResponseEntity<>(commentService.createComment(postId,commentDTO), HttpStatus.CREATED);
    }
    @GetMapping("/comments")
    public List<CommentDTO> getCommentsByPostId(@PathVariable(value = "postId")  long postId){
        return  commentService.getCommentsByPostId(postId);
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable(value = "postId")  long postId, @PathVariable(value = "id")  long id){
        CommentDTO commentDTO = commentService.getCommentById(postId, id);
        return new ResponseEntity<>(commentDTO, HttpStatus.OK);
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable(value = "postId")  long postId, @PathVariable(value = "id")  long id, @RequestBody CommentDTO commentDTO){

        CommentDTO updatedCommentDTO = commentService.updateComment(postId, id, commentDTO);
        return new ResponseEntity<>(updatedCommentDTO, HttpStatus.OK);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<String> updateComment(@PathVariable(value = "postId")  long postId, @PathVariable(value = "id")  long id){
        commentService.deleteComment(postId, id);
        return new ResponseEntity<>("Comment was successfully deleted", HttpStatus.OK);
    }
}
