package com.yt.jpa.hospitalManagement.mapper;

import com.yt.jpa.hospitalManagement.dto.request.DoctorRequestDto;
import com.yt.jpa.hospitalManagement.entity.Doctor;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {
    public Doctor toEntity(DoctorRequestDto dto) {
        Doctor doctor = new Doctor();
        doctor.setName(dto.getName());
        doctor.setEmail(dto.getEmail());
        doctor.setPhone(dto.getPhone());
        doctor.setDob(dto.getDob());
        doctor.setGender(dto.getGender());
        doctor.setAddress(dto.getAddress());

        return doctor;
    }

    public Doctor updateEntity(DoctorRequestDto dto, Doctor doctor) {
        doctor.setName(dto.getName());
        doctor.setEmail(dto.getEmail());
        doctor.setPhone(dto.getPhone());
        doctor.setDob(dto.getDob());
        doctor.setGender(dto.getGender());
        doctor.setAddress(dto.getAddress());

        return doctor;
    }
}
