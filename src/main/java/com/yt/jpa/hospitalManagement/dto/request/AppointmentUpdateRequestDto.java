package com.yt.jpa.hospitalManagement.dto.request;

import com.yt.jpa.hospitalManagement.enums.AppointmentStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentUpdateRequestDto {
    @NotBlank
    private Long doctorId;

    @NotBlank
    private AppointmentStatus status;
}
