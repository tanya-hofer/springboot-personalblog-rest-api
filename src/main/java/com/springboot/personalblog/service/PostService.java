package com.springboot.personalblog.service;

import com.springboot.personalblog.DTO.PostDTO;
import com.springboot.personalblog.DTO.PostResponse;

public interface PostService {
    PostDTO createPost(PostDTO postDTO);

    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    PostDTO getPostById(long id);

    PostDTO updatePost(PostDTO postDTO, long id);

    void deletePostById(long id);
}
