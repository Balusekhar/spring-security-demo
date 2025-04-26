package com.balutirupati.securitydemo.repositories;

import com.balutirupati.securitydemo.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostRepo extends JpaRepository<PostEntity, UUID> {
}
