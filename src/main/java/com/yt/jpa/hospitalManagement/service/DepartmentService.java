package com.yt.jpa.hospitalManagement.service;

import com.yt.jpa.hospitalManagement.dto.request.patch.DepartmentPatchRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.DepartmentRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.DepartmentResponseDto;

import java.util.List;

public interface DepartmentService {
    List<DepartmentResponseDto> getAllDepartments();

    DepartmentResponseDto getDepartmentById(Long id);

    DepartmentResponseDto createDepartment(DepartmentRequestDto departmentRequestDto);

    DepartmentResponseDto updateDepartment(Long id, DepartmentRequestDto departmentRequestDto);

    DepartmentResponseDto updatePartialDepartment(Long id, DepartmentPatchRequestDto departmentPatchRequestDto);

    void deleteDepartment(Long id);
}
