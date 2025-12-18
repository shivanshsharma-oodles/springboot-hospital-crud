package com.yt.jpa.hospitalManagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.sql.Update;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequestDto {
//    @NotNull(message = "Patient Id is required")
    private Long patientId;

    @NotNull(message = "Patient Id is required")
    private Long doctorId;
}
