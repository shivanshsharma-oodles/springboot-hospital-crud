package com.yt.jpa.hospitalManagement.controller;

import com.yt.jpa.hospitalManagement.dto.request.SignupRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.LoginResponseDto;
import com.yt.jpa.hospitalManagement.dto.response.SignupResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import com.yt.jpa.hospitalManagement.security.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yt.jpa.hospitalManagement.dto.request.LoginRequestDto;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto){
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto dto){
        return ResponseEntity.ok(authService.registerUser(dto));
    }

    @PostMapping("/admin/login")
    public ResponseEntity<LoginResponseDto> adminLogin(@RequestBody LoginRequestDto dto){
        return ResponseEntity.ok(authService.adminLogin(dto));
    }
}
