package com.yt.jpa.hospitalManagement.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                "http://localhost:5173"
        ));
        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        config.setAllowedHeaders(List.of("*"));
//        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        JwtAuthenticationEntryPoint entryPoint = new JwtAuthenticationEntryPoint();
        JwtAccessDeniedHandler accessDeniedHandler = new JwtAccessDeniedHandler();
        //        This httpSecurity object is used to configure web based security for specific http requests.
        httpSecurity
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(entryPoint)
                                .accessDeniedHandler(accessDeniedHandler)


                )
                .cors(Customizer.withDefaults())
                .csrf(csrfConfig -> csrfConfig.disable()) // csrf disabled
                .sessionManagement(sessionConfig -> // session management configuration
                        sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // authorizeHttpRequests method is used to restrict access based on the HttpServletRequest
                .authorizeHttpRequests(auth -> auth

//                        // Auth
                                .requestMatchers("/auth/me").authenticated() // authenticated only
                                .requestMatchers("/auth/**").permitAll() // Public endpoints

//                        Public Read-Only Endpoints
                                .requestMatchers(HttpMethod.GET,
                                        "/departments/**",
                                        "/doctors",
                                        "/doctors/*",
                                        "/doctors/department/*"
                                ).permitAll()

//                        Patient & Doctor Only
                                .requestMatchers("/appointments/me").hasAnyRole("DOCTOR", "PATIENT")
                                .requestMatchers(HttpMethod.PUT, "/appointments/*/cancel").hasAnyRole("DOCTOR", "PATIENT")

//                        PATIENT AND ADMIN
                                .requestMatchers(HttpMethod.GET, "/doctors/*/slots").hasAnyRole("ADMIN", "PATIENT")


//                        Patient Only
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/appointments"
                                ).hasAnyRole("PATIENT")

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/bills/me",
                                        "/bills/appointment/*"
                                ).hasAnyRole("PATIENT")

                                // Patient Controller
                                .requestMatchers(HttpMethod.GET, "/patients/me").hasRole("PATIENT")
                                .requestMatchers(HttpMethod.POST, "/patients").hasRole("PATIENT")
                                .requestMatchers(HttpMethod.PUT, "/patients").hasRole("PATIENT")
                                .requestMatchers(HttpMethod.PATCH, "/patients").hasRole("PATIENT")


//                        Doctor Only
                                // Update Status
                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/appointments/*/status",
                                        "/appointments/*/complete"
                                ).hasRole("DOCTOR")

                                // Create Bill
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/bills",
                                        "/doctors/slots"
                                ).hasRole("DOCTOR")

                                // Delete Slot
                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/doctors/slots/*"
                                ).hasRole("DOCTOR")

                                // Get and Update Doctor
                                .requestMatchers(HttpMethod.GET, "/doctors/me", "/doctors/slots").hasRole("DOCTOR")
                                .requestMatchers(HttpMethod.PATCH, "/doctors").hasRole("DOCTOR")
                                .requestMatchers(HttpMethod.PUT, "/doctors").hasRole("DOCTOR")


//                        Admin Only
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/appointments/admin/**",
                                        "/bills/admin/**",
                                        "/doctors/admin/**",
                                        "/patients/admin/**",
                                        "/medical-records/admin/**"
                                ).hasRole("ADMIN")

                                // Create Doctor
                                .requestMatchers(HttpMethod.POST, "/doctors").hasRole("ADMIN")

                                // Department Write
                                .requestMatchers("/departments/**"
                                ).hasRole("ADMIN")

                                .anyRequest().hasRole("ADMIN")
//                              .anyRequest().authenticated()
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
    