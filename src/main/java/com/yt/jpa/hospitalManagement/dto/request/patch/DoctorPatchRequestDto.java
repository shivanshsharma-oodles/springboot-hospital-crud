package com.yt.jpa.hospitalManagement.dto.request.patch;

import com.yt.jpa.hospitalManagement.embeddable.Address;
import com.yt.jpa.hospitalManagement.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorPatchRequestDto {
    private String name;

    @Email(message = "Enter Valid Email")
    private String email;

    @Size(min = 8, max = 15)
    private String phone;

    private LocalDate dob;
    private Gender gender;
    private Address address;
    private Long departmentId;
}
