package com.yt.jpa.hospitalManagement.dto.summary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordSummaryDto {
    private Long id;
    private DoctorSummaryDto doctor;
    private PatientSummaryDto patient;
    private AppointmentSummaryDto appointment;
    private String diagnosis; // Useful for the list view title
    private LocalDateTime createdAt;
}
