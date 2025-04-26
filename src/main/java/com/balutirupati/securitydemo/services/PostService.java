package com.balutirupati.securitydemo.services;

import com.balutirupati.securitydemo.entities.PostEntity;
import com.balutirupati.securitydemo.repositories.PostRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    public final PostRepo postRepo;

    public PostService(PostRepo postRepo) {
        this.postRepo = postRepo;
    }

    public List<PostEntity> getAllposts() {
        return postRepo.findAll();
    }

    public PostEntity createpost(PostEntity postDetails) {
        return postRepo.save(postDetails);
    }
}
