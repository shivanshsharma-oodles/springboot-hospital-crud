package com.yt.jpa.hospitalManagement.mapper;

import com.yt.jpa.hospitalManagement.dto.request.PatientRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.PatientResponseDto;
import com.yt.jpa.hospitalManagement.entity.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {
    public Patient toEntity(PatientRequestDto dto){
        Patient patient = new Patient();

        patient.setName(dto.getName());
        patient.setPhone(dto.getPhone());
        patient.setDob(dto.getDob());
        patient.setGender(dto.getGender());
        patient.setAddress(dto.getAddress());
        return patient;
    }

    public Patient updateEntity(PatientRequestDto dto, Patient patient){
        patient.setName(dto.getName());
        patient.setPhone(dto.getPhone());
        patient.setDob(dto.getDob());
        patient.setGender(dto.getGender());
        patient.setAddress(dto.getAddress());
        return patient;
    }

    public PatientResponseDto toResponseDto(Patient patient){
        PatientResponseDto dto = new PatientResponseDto();

        dto.setName(patient.getName());
        dto.setPhone(patient.getPhone());
        dto.setEmail(patient.getUser().getEmail());
        dto.setDob(patient.getDob());
        dto.setGender(patient.getGender());
        dto.setAddress(patient.getAddress());
        dto.setCreatedAt(patient.getCreatedAt());
        return dto;
    }
}
