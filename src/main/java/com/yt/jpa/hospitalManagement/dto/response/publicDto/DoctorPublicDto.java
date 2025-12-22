package com.yt.jpa.hospitalManagement.dto.response.publicDto;

import com.yt.jpa.hospitalManagement.dto.response.DepartmentResponseDto;
import com.yt.jpa.hospitalManagement.enums.DoctorStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class DoctorPublicDto {
    private Long id;
    private String name;
    private DoctorStatus status;
    private DepartmentResponseDto department;
}