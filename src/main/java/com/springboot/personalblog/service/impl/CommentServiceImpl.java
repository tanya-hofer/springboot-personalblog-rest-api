package com.springboot.personalblog.service.impl;

import com.springboot.personalblog.DTO.CommentDTO;
import com.springboot.personalblog.exception.BlogAPIException;
import com.springboot.personalblog.exception.ResourceNotFoundException;
import com.springboot.personalblog.model.Comment;
import com.springboot.personalblog.model.Post;
import com.springboot.personalblog.repository.CommentRepository;
import com.springboot.personalblog.repository.PostRepository;
import com.springboot.personalblog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private ModelMapper mapper;
    private PostRepository postRepository;
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public CommentDTO createComment(long postId, CommentDTO commentDTO) {
        Comment comment = mapper.map(commentDTO,Comment.class);
        //retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "id", postId));

        // set post to comment entity
        comment.setPost(post);

        //save comment entity into db
        Comment createdComment = commentRepository.save(comment);

        return  mapper.map(createdComment,CommentDTO.class);
    }

    @Override
    public CommentDTO getCommentById(long postId, long commentId) {
        //retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "id", postId));
        //retrieve comment entity by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment", "id", commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment doesn't belong to post");
        }

        return mapper.map(comment,CommentDTO.class);
    }

    @Override
    public CommentDTO updateComment(long postId, long commentId, CommentDTO commentRequest) {
        //retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "id", postId));
        //retrieve comment entity by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment", "id", commentId));

        // comment doesn't belong to post
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment doesn't belong to post");
        }
        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setMessageBody(commentRequest.getMessageBody());
        Comment updatedComment = commentRepository.save(comment);
        return mapper.map(updatedComment,CommentDTO.class);
    }

    @Override
    public void deleteComment(long postId, long commentId) {
        //retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "id", postId));
        //retrieve comment entity by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment", "id", commentId));
        // comment doesn't belong to post
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment doesn't belong to post");
        }

        commentRepository.delete(comment);
    }

    @Override
    public List<CommentDTO> getCommentsByPostId(long postId) {
        // retrive comments by postId
        List<Comment> comments = commentRepository.findByPostId(postId);
        // convert list of comment entities to list of comment dto's
        return comments.stream().map(comment ->  mapper.map(comment,CommentDTO.class)).collect(Collectors.toList());
    }
}
