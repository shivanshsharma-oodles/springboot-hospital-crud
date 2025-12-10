package com.yt.jpa.hospitalManagement.dto.request;

import com.yt.jpa.hospitalManagement.embeddable.Address;
import com.yt.jpa.hospitalManagement.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientPatchRequestDto {

    private String name;

    @Email(message = "Enter valid email id")
    private String email;
    private Address address;

    @Size(min = 8, max = 15)
    private String phone;

    private LocalDate dob;
    private Gender gender;
}