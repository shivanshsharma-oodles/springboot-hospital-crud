package com.yt.jpa.hospitalManagement.service;

import com.yt.jpa.hospitalManagement.dto.request.patch.PatientPatchRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.PatientRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.PatientResponseDto;

import java.util.List;

public interface PatientService {
    List<PatientResponseDto> findAllPatients();

    PatientResponseDto findPatientById(Long id);

    PatientResponseDto createPatient(String email, PatientRequestDto patientRequestDto);

    PatientResponseDto updatePatient(Long id, PatientRequestDto patientRequestDto);

    PatientResponseDto updatePartialPatient(Long id, PatientPatchRequestDto patientPatchRequestDto);

    Void deletePatient(Long id);
}
