package com.balutirupati.securitydemo.controllers;

import com.balutirupati.securitydemo.entities.PostEntity;
import com.balutirupati.securitydemo.services.PostService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PostController {

  public final PostService postService;

  public PostController(PostService postService) {
    this.postService = postService;
  }

  @GetMapping(value = "")
  public String hello() {
    return "Hello World!";
  }

  @GetMapping(value = "/posts")
  public List<PostEntity> getPosts() {
    return postService.getAllposts();
  }

  @PostMapping(value = "/posts")
  @PreAuthorize("hasRole('CREATOR')")
  public PostEntity createPosts(@RequestBody PostEntity postDetails) {
    return postService.createpost(postDetails);
  }
}
