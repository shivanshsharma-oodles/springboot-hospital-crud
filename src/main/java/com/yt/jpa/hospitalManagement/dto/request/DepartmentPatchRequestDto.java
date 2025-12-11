package com.yt.jpa.hospitalManagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentPatchRequestDto {
    @Size(min = 2, max = 20)
    private String name;

    @Size(min = 10)
    private String description;
}