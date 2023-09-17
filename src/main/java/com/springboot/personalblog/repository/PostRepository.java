package com.springboot.personalblog.repository;

import com.springboot.personalblog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
    //
}
