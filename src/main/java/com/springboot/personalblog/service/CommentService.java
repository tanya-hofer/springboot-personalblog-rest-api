package com.springboot.personalblog.service;

import com.springboot.personalblog.DTO.CommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(long postId, CommentDTO commentDTO);

    List<CommentDTO>  getCommentsByPostId(long postId);

    CommentDTO  getCommentById(long postId, long commentId);

    CommentDTO updateComment(long postId, long commentId, CommentDTO commentRequest);

    void deleteComment(long postId, long commentId);
}
