package com.yt.jpa.hospitalManagement.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequestDto {

    @NotNull(message = "Doctor slot is required")
    private Long doctorSlotId;
}