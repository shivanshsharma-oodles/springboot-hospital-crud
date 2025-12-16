package com.yt.jpa.hospitalManagement.security;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
    public  class WebSecurityConfig {

        private final JwtAuthFilter jwtAuthFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    //        This httpSecurity object is used to configure web based security for specific http requests.
            httpSecurity

                    .csrf(csrfConfig -> csrfConfig.disable()) // csrf disabled
                    .sessionManagement(sessionConfig -> // session management configuration
                            sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    // authorizeHttpRequests method is used to restrict access based on the HttpServletRequest
                    .authorizeHttpRequests(auth -> auth

                            // Auth
                            .requestMatchers("/auth/**").permitAll() // Public endpoints

                            // Public Read-Only Endpoints
                            .requestMatchers(HttpMethod.GET,
                                    "/departments/**",
                                    "/doctors/**"
                            ).permitAll()

//                            // DOCTOR & Admin ONLY
//                            .requestMatchers("/doctors/**").hasAnyRole("DOCTOR", "ADMIN")
//
//                            // PATIENT & Admin ONLY
//                            .requestMatchers("/patients/**").hasAnyRole("PATIENT", "ADMIN")

//                            // DOCTOR and PATIENT & Admin
//                            .requestMatchers(
//                                    "/appointments/**",
//                                    "/bills/**",
//                                    "/medical-records/**"
//                            ).hasAnyRole("DOCTOR", "PATIENT", "ADMIN")

                            // ADMIN ONLY
//                            .anyRequest().hasRole("ADMIN")
                            .anyRequest().authenticated()
                    )
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            ;

            return httpSecurity.build();
        }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
    