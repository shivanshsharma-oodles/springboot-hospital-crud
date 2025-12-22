package com.yt.jpa.hospitalManagement.dto.request;

import com.yt.jpa.hospitalManagement.embeddable.Address;
import com.yt.jpa.hospitalManagement.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCreateDoctorRequestDto {

    // --- USER (Auth) ---
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    // --- DOCTOR (Domain) ---
    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @NotNull
    private Long departmentId;

    private LocalDate dob;
    private Gender gender;
    private Address address;
}

