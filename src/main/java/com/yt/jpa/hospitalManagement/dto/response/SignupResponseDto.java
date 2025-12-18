package com.yt.jpa.hospitalManagement.dto.response;

import com.yt.jpa.hospitalManagement.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupResponseDto {
    private Long userId;
    private String email;
}
