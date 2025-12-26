package com.yt.jpa.hospitalManagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordRequestDto {

    @NotBlank(message = "Please provide symptoms")
    private String symptoms;

    @NotBlank(message = "Please provide diagnosis")
    private String diagnosis;

    private String treatment;

    @NotBlank(message = "Please provide prescription")
    private String prescription;

    private LocalDate followUpDate;

    private Double temperature;
    private Integer pulse;
    private Integer bpSystolic;
    private Integer bpDiastolic;
}