package com.devqoo.backend.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController implements UserApiDocs {

    @Override
    @GetMapping
    public ResponseEntity<String> testGet() {
        return ResponseEntity.ok("OK");
    }
}
