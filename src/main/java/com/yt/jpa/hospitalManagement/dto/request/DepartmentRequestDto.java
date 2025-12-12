package com.yt.jpa.hospitalManagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentRequestDto {
    @NotBlank(message = "Department name can not be empty")
    @Size(min = 1, max = 40)
    private String name;

    @NotBlank(message = "Description is required")
    @Size(min = 10)
    private String description;
}