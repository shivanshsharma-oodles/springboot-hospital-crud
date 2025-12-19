package com.yt.jpa.hospitalManagement.config;

import com.yt.jpa.hospitalManagement.entity.User;
import com.yt.jpa.hospitalManagement.enums.Role;
import com.yt.jpa.hospitalManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPass;

    @Override
    public void run(String... args){

//        Check: admin already exists
        if(userRepository.existsByEmail(adminEmail)){
            return;
        }

        User admin = User.builder()
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPass))
                .roles(Set.of(Role.ADMIN))
                .build();

        userRepository.save(admin);

        log.info("Admin user seeded successfully with email: {}", adminEmail);
    }


}
