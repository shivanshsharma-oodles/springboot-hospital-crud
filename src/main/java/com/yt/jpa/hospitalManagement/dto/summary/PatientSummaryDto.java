package com.yt.jpa.hospitalManagement.dto.summary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientSummaryDto {
    private Long id;
    private String name;
}
