package com.yt.jpa.hospitalManagement.security;

import com.yt.jpa.hospitalManagement.entity.User;
import com.yt.jpa.hospitalManagement.enums.Role;
import com.yt.jpa.hospitalManagement.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
@Slf4j // For logging
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final AuthUtil authUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        log.info("Incoming request: {}", request.getRequestURI());

//        take JWT out of request header
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("JWT Token is missing or does not begin with Bearer String");
            filterChain.doFilter(request, response); // pass the request to next filter
            return;
        }

        String token = authHeader.substring(7); // remove "Bearer " prefix
        String email = authUtil.getUsernameFromToken(token);



        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

            Set<Role> roles = user.getRoles();

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            roles.stream()
                                    .map(role -> new SimpleGrantedAuthority(role.name()))
                                    .toList()
                    );

            // Set this authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Authenticated user: {} , setting security context", email);
        }
        filterChain.doFilter(request, response); // pass the request to next filter
    }
}
