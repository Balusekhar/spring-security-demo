package com.balutirupati.securitydemo;

import com.balutirupati.securitydemo.config.JwtService;
import com.balutirupati.securitydemo.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class SecuritydemoApplicationTests {

  @Autowired
  private JwtService JwtService;

  @Test
  void contextLoads() {
  }

  @Test
  void testJWT() {
    UserEntity user = new UserEntity(UUID.fromString("48333906-df77-41c3-8954-77a754eeee8e"), "balu@gmail.com", "balu", "1234", 8);
    String token = JwtService.generateAccessToken(user);
    System.out.println(token);

    String id = JwtService.parseJwt(token);
    System.out.println(id);
  }

}
