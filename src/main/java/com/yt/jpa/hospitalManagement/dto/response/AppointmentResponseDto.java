package com.yt.jpa.hospitalManagement.dto.response;

import com.yt.jpa.hospitalManagement.dto.summary.BillSummaryDto;
import com.yt.jpa.hospitalManagement.dto.summary.DoctorSummaryDto;
import com.yt.jpa.hospitalManagement.dto.summary.PatientSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponseDto {
    private Long id;

    private DoctorSummaryDto doctor;
    private PatientSummaryDto patient;
    private BillSummaryDto bill;
}