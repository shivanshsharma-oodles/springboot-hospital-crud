package com.yt.jpa.hospitalManagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorSlotResponseDto {
    private Long id;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
}
