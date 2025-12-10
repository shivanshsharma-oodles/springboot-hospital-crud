package com.yt.jpa.hospitalManagement.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.yt.jpa.hospitalManagement.enums.Gender;
import com.yt.jpa.hospitalManagement.embeddable.Address;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponseDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private LocalDate dob;
    private Gender gender;
    private Address address;
    private DepartmentResponseDto department;
    private LocalDateTime createdAt;
}