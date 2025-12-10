package com.yt.jpa.hospitalManagement.dto.response;

import com.yt.jpa.hospitalManagement.enums.DepartmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponseDto {
    private Long id;
    private String name;
    private String description;
    private DepartmentStatus departmentStatus;
}