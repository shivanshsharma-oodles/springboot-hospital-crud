package com.yt.jpa.hospitalManagement.mapper;

import com.yt.jpa.hospitalManagement.dto.request.DoctorRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.DoctorResponseDto;
import com.yt.jpa.hospitalManagement.entity.Doctor;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {
    public Doctor toEntity(DoctorRequestDto dto) {
        Doctor doctor = new Doctor();
        doctor.setName(dto.getName());
        doctor.setPhone(dto.getPhone());
        doctor.setDob(dto.getDob());
        doctor.setGender(dto.getGender());
        doctor.setAddress(dto.getAddress());

        return doctor;
    }

    public Doctor updateEntity(DoctorRequestDto dto, Doctor doctor) {
        doctor.setName(dto.getName());
//        doctor.setEmail(dto);
        doctor.setPhone(dto.getPhone());
        doctor.setDob(dto.getDob());
        doctor.setGender(dto.getGender());
        doctor.setAddress(dto.getAddress());

        return doctor;
    }

    public DoctorResponseDto toResponseDto(Doctor doctor) {
        DoctorResponseDto dto = new DoctorResponseDto();
        dto.setId(doctor.getId());
        dto.setName(doctor.getName());
        dto.setEmail(doctor.getUser().getEmail());
        dto.setPhone(doctor.getPhone());
        dto.setDob(doctor.getDob());
        dto.setGender(doctor.getGender());
        dto.setAddress(doctor.getAddress());
        dto.setStatus(doctor.getStatus());
        dto.setCreatedAt(doctor.getCreatedAt());
        dto.setUpdatedAt(doctor.getUpdatedAt());

        return dto;
    }

}
