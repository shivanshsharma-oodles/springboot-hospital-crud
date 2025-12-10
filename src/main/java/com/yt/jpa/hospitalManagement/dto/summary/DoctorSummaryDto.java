package com.yt.jpa.hospitalManagement.dto.summary;

import com.yt.jpa.hospitalManagement.enums.DoctorStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorSummaryDto {
    private Long id;
    private String name;
    private DoctorStatus status;
}

