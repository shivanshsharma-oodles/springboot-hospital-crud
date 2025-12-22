package com.yt.jpa.hospitalManagement.dto.response;

import com.yt.jpa.hospitalManagement.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserDto {
    private Long id;
    private String email;
    private Set<Role> roles;
}
