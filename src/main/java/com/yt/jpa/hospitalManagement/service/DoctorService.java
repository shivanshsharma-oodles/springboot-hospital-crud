package com.yt.jpa.hospitalManagement.service;

import com.yt.jpa.hospitalManagement.dto.request.DoctorPatchRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.DoctorRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.DoctorResponseDto;

import java.util.List;

public interface DoctorService {
    List<DoctorResponseDto> getAllDoctors();

    DoctorResponseDto getDoctorsById(Long id);

    DoctorResponseDto createDoctor(DoctorRequestDto doctorRequestDto);

    DoctorResponseDto updateDoctor(Long id, DoctorRequestDto doctorRequestDto);

    DoctorResponseDto updatePartialDoctor(Long id, DoctorPatchRequestDto doctorPatchRequestDto);

    Void deleteDoctor(Long id);


}
