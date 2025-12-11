package com.yt.jpa.hospitalManagement.dto.response;

import com.yt.jpa.hospitalManagement.dto.summary.AppointmentSummaryDto;
import com.yt.jpa.hospitalManagement.dto.summary.PatientSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillResponseDto {
    private Long id;
    private PatientSummaryDto patient;
    private AppointmentSummaryDto appointment;
    private Double amount;
    private LocalDateTime createdAt;
}