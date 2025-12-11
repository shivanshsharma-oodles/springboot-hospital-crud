package com.yt.jpa.hospitalManagement.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillRequestDto {
    @NotNull
    private Long patientId;

    @NotNull
    private Long appointmentId;

    @NotNull
    private Double amount;
}
