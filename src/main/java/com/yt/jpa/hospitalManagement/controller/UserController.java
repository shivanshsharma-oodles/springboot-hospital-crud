package com.yt.jpa.hospitalManagement.controller;

import com.yt.jpa.hospitalManagement.entity.User;
import com.yt.jpa.hospitalManagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody User user){
        return ResponseEntity.ok(userService.authenticate(user));
    }

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody User user){
        return ResponseEntity.ok(userService.registerUser(user));
    }
}
