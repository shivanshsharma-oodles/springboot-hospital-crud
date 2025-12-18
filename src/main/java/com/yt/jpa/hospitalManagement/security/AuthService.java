package com.yt.jpa.hospitalManagement.security;

import com.yt.jpa.hospitalManagement.dto.request.LoginRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.SignupRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.LoginResponseDto;
import com.yt.jpa.hospitalManagement.dto.response.SignupResponseDto;
import com.yt.jpa.hospitalManagement.entity.Patient;
import com.yt.jpa.hospitalManagement.entity.User;
import com.yt.jpa.hospitalManagement.enums.Role;
import com.yt.jpa.hospitalManagement.repository.PatientRepository;
import com.yt.jpa.hospitalManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final AuthUtil authUtil;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final PatientRepository patientRepository;

    public LoginResponseDto login(LoginRequestDto dto) {

        // this authentication manager will try to authenticate the user with given email and password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        // Authenticated
        User user = (User) authentication.getPrincipal(); // get the authenticated user details and it will help in creating token

        // Generate Token
        String token = authUtil.generateToken(user);

        // Return response with token and user id
        return new LoginResponseDto(token, user.getId());
    }

    public SignupResponseDto registerUser(SignupRequestDto dto){
        User user = userRepository.findByEmail(dto.getEmail()).orElse(null);

        if(user != null){
            throw new RuntimeException("User already exists with email: " + dto.getEmail());
        }

        user = userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .roles(Set.of(Role.PATIENT)) // default role
                .build()
        );

        Patient patient = Patient.builder()
                .name(dto.getName())
                .phone(dto.getPhone())
                .user(user)
                .build();

        patientRepository.save(patient);

        return modelMapper.map(
                user,
                SignupResponseDto.class);
    }
}