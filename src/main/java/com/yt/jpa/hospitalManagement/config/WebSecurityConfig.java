package com.yt.jpa.hospitalManagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


// B Crypt Password Encoder Bean Configuration
@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        This httpSecurity object is used to configure web based security for specific http requests.
        httpSecurity

                // authorizeHttpRequests method is used to restrict access based on the HttpServletRequest
                .authorizeHttpRequests(auth -> auth

                        // Auth
                        .requestMatchers("/auth/**").permitAll() // Public endpoints

                        // Public Read-Only Endpoints
                        .requestMatchers(HttpMethod.GET,
                                "/departments/**",
                                "/doctors/**"
                        ).permitAll()

                        // DOCTOR & Admin ONLY
                        .requestMatchers("/doctors/**").hasAnyRole("DOCTOR", "ADMIN")

                        // PATIENT & Admin ONLY
                        .requestMatchers("/patients/**").hasAnyRole("PATIENT", "ADMIN")

                        // DOCTOR and PATIENT & Admin
                        .requestMatchers(
                                "/appointments/**",
                                "/bills/**",
                                "/medical-records/**"
                        ).hasAnyRole("DOCTOR", "PATIENT", "ADMIN")

                        // ADMIN ONLY
                        .anyRequest().hasRole("ADMIN")
                );

        return httpSecurity.build();
    }
}

