package com.yt.jpa.hospitalManagement.service.impl;

import com.yt.jpa.hospitalManagement.dto.request.DepartmentPatchRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.DepartmentRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.DepartmentResponseDto;
import com.yt.jpa.hospitalManagement.entity.Department;
import com.yt.jpa.hospitalManagement.enums.DepartmentStatus;
import com.yt.jpa.hospitalManagement.exception.DuplicateResourceException;
import com.yt.jpa.hospitalManagement.exception.ResourceNotFoundException;
import com.yt.jpa.hospitalManagement.repository.DepartmentRepository;
import com.yt.jpa.hospitalManagement.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    /* Get All Departments */
    @Override
    public List<DepartmentResponseDto> getAllDepartments(){
        List<Department>  departments = departmentRepository.findByStatusNot(DepartmentStatus.ARCHIVED);

//        Converting Department Entity to DTO
        return departments.stream()
                .map(dept -> modelMapper.map(dept, DepartmentResponseDto.class))
                .toList();
    }

    /* Get Departments from Id */
    @Override
    public DepartmentResponseDto getDepartmentById(Long id){
        Department department = departmentRepository.findByIdAndStatusNot(id, DepartmentStatus.ARCHIVED)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        return modelMapper.map(department, DepartmentResponseDto.class);
    }

    /* Create Department */
    @Override
    public DepartmentResponseDto createDepartment(DepartmentRequestDto  departmentRequestDto) {
        if(departmentRepository.existsByNameAndStatusNot(departmentRequestDto.getName(), DepartmentStatus.ARCHIVED)){
            throw new DuplicateResourceException("Department already exists");
        }
        Department department = modelMapper.map(departmentRequestDto, Department.class);

        return modelMapper.map(departmentRepository.save(department), DepartmentResponseDto.class);
    }

    /* Update Department */
    @Override
    public DepartmentResponseDto updateDepartment(Long id, DepartmentRequestDto departmentRequestDto){

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        if(departmentRepository.existsByNameAndStatusNotAndIdNot(departmentRequestDto.getName(), DepartmentStatus.ARCHIVED, id)){
            throw new DuplicateResourceException("Department already exists");
        }

        // Map Dept Req Dto to department (Department Entity)
        modelMapper.map(departmentRequestDto,  department);
        return modelMapper.map(departmentRepository.save(department), DepartmentResponseDto.class);
    }

    /* Update Partial Department */
    @Override
    public DepartmentResponseDto updatePartialDepartment(Long id, DepartmentPatchRequestDto departmentPatchRequestDto){

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        if(departmentPatchRequestDto.getName() != null && !departmentPatchRequestDto.getName().isEmpty()){
            if(departmentRepository.existsByNameAndStatusNotAndIdNot(departmentPatchRequestDto.getName(), DepartmentStatus.ARCHIVED, id)){
                throw new DuplicateResourceException("Department already exists");
            }
            department.setName(departmentPatchRequestDto.getName());
        }

        if (departmentPatchRequestDto.getDescription() != null && !departmentPatchRequestDto.getDescription().isEmpty()){
            department.setDescription(departmentPatchRequestDto.getDescription());
        }

        return modelMapper.map(departmentRepository.save(department), DepartmentResponseDto.class);
    }

    /* Delete Department */
    @Override
    public void deleteDepartment(Long id){
        Department department = departmentRepository.findByIdAndStatusNot(id, DepartmentStatus.ARCHIVED)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        department.setDepartmentStatus(DepartmentStatus.ARCHIVED);
        departmentRepository.save(department);
    }
}
