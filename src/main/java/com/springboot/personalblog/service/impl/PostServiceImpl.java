package com.springboot.personalblog.service.impl;

import com.springboot.personalblog.DTO.PostDTO;
import com.springboot.personalblog.DTO.PostResponse;
import com.springboot.personalblog.exception.ResourceNotFoundException;
import com.springboot.personalblog.model.Post;
import com.springboot.personalblog.repository.PostRepository;
import com.springboot.personalblog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private ModelMapper mapper;

    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper) {
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public PostDTO createPost(PostDTO postDTO) {
        Post post = mapper.map(postDTO,Post.class);
        Post createdPost = postRepository.save(post);
        return mapper.map(createdPost,PostDTO.class);
    }
    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir){
        //create pageable instance
        PageRequest pageable = PageRequest.of(pageNo, pageSize,
                sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Page<Post> posts = postRepository.findAll(pageable);

        //get content for page object
        List<Post> listOfPosts = posts.getContent();
        List<PostDTO> content = listOfPosts.stream().map(post -> mapper.map(post,PostDTO.class)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse(content,pageNo, pageSize, posts.getTotalElements(), posts.getTotalPages(), posts.isLast());

        return postResponse;
    }

    @Override
    public PostDTO getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post", "id", id));
        return mapper.map(post,PostDTO.class);
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, long id) {
        //get post by id from DB
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post", "id", id));

        //update fields post
        post.setContent(postDTO.getContent());
        post.setDescription(postDTO.getDescription());
        post.setTitle(postDTO.getTitle());
        Post updatedPost = postRepository.save(post);

        return mapper.map(updatedPost,PostDTO.class);
    }

    @Override
    public void deletePostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
    }
}
