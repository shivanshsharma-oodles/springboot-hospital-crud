package com.yt.jpa.hospitalManagement.dto.request;

import com.yt.jpa.hospitalManagement.embeddable.Address;
import com.yt.jpa.hospitalManagement.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

//    @Email(message = "Enter valid email id")
//    @NotBlank(message = "Email cannot be blank")
//    private String email;

    private Address address;

    @NotBlank(message = "Phone can not be blank")
    @Size(min = 8, max = 15)
    private String phone;

    @NotNull(message = "Date of birth required")
    private LocalDate dob;

    @NotNull(message = "Gender is required")
    private Gender gender;
}
