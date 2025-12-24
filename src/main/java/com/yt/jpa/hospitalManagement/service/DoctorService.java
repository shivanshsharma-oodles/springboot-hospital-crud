package com.yt.jpa.hospitalManagement.service;

import com.yt.jpa.hospitalManagement.dto.request.AdminCreateDoctorRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.DoctorSlotRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.patch.DoctorPatchRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.DoctorRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.DoctorResponseDto;
import com.yt.jpa.hospitalManagement.dto.response.DoctorSlotResponseDto;
import com.yt.jpa.hospitalManagement.dto.response.publicDto.DoctorPublicDto;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface DoctorService {
    List<DoctorPublicDto> getAllDoctors();

    DoctorResponseDto getDoctorsById(Long id);
    DoctorPublicDto getDoctorPubliclyById(Long id);

    List<DoctorPublicDto> getDoctorsByDepartmentId(Long departmentId);

    DoctorResponseDto createDoctor(String email, DoctorRequestDto doctorRequestDto);

    DoctorResponseDto updateDoctor(Long id, DoctorRequestDto doctorRequestDto);

    DoctorResponseDto updatePartialDoctor(Long id, DoctorPatchRequestDto doctorPatchRequestDto);

    void deleteDoctor(Long id);

    DoctorResponseDto createDoctorByAdmin(AdminCreateDoctorRequestDto dto);

    void createDoctorSlot(Long userId,  DoctorSlotRequestDto dto);

    List<DoctorSlotResponseDto> getSlotsByDoctor(Long doctorId);

}
