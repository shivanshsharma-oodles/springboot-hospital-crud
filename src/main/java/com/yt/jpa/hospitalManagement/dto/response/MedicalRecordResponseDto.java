package com.yt.jpa.hospitalManagement.dto.response;

import com.yt.jpa.hospitalManagement.dto.summary.AppointmentSummaryDto;
import com.yt.jpa.hospitalManagement.dto.summary.DoctorSummaryDto;
import com.yt.jpa.hospitalManagement.dto.summary.PatientSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordResponseDto {
    private Long id;
    private DoctorSummaryDto doctor;
    private PatientSummaryDto patient;
    private AppointmentSummaryDto appointment;

    private String symptoms;
    private String diagnosis;
    private String treatment;
    private String prescription;
    private LocalDate followUpDate;

    private Double temperature;
    private Integer pulse;
    private Integer bpSystolic;
    private Integer bpDiastolic;

    private LocalDateTime createdAt;
}