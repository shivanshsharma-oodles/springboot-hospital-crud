package com.yt.jpa.hospitalManagement.dto.response;

import com.yt.jpa.hospitalManagement.embeddable.Address;
import com.yt.jpa.hospitalManagement.enums.Gender;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponseDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private LocalDate dob;
    private Gender gender;
    private Address address;
    private LocalDateTime createdAt;
}