package com.yt.jpa.hospitalManagement.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordRequestDto {

    private String symptoms;
    private String diagnosis;
    private LocalDate followUpDate;

    private Double temperature;
    private Integer pulse;
    private Integer bpSystolic;
    private Integer bpDiastolic;
}