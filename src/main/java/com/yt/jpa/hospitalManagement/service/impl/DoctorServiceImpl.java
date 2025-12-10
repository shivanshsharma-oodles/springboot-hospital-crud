package com.yt.jpa.hospitalManagement.service.impl;

import com.yt.jpa.hospitalManagement.dto.request.DoctorPatchRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.DoctorRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.DoctorResponseDto;
import com.yt.jpa.hospitalManagement.entity.Doctor;
import com.yt.jpa.hospitalManagement.exception.DuplicateResourceException;
import com.yt.jpa.hospitalManagement.exception.ResourceNotFoundException;
import com.yt.jpa.hospitalManagement.repository.DepartmentRepository;
import com.yt.jpa.hospitalManagement.repository.DoctorRepository;
import com.yt.jpa.hospitalManagement.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    /* Get All Doctors */
    @Override
    public List<DoctorResponseDto> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();

//        Convert Doctor to Doctor Response Dto
        return doctors.stream()
                .map(d -> modelMapper.map(d, DoctorResponseDto.class))
                .toList();
    }

    /* Get Doctor By Id */
    @Override
    public DoctorResponseDto getDoctorsById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor does not exist"));

        return modelMapper.map(doctor, DoctorResponseDto.class);
    }

    /* Create Doctor */
    @Override
    public DoctorResponseDto createDoctor(DoctorRequestDto doctorRequestDto) {
        if(doctorRepository.existsByEmail(doctorRequestDto.getEmail())){
            throw new DuplicateResourceException("Doctor already exists with same email");
        }
        if(doctorRepository.existsByPhone(doctorRequestDto.getPhone())){
            throw new DuplicateResourceException("Doctor already exists with same phone");
        }

        // Load Dept from DB



        Doctor doctor = modelMapper.map(doctorRequestDto, Doctor.class);

        return modelMapper.map(doctorRepository.save(doctor), DoctorResponseDto.class);
    }

    /* Update Doctor */
    @Override
    public DoctorResponseDto updateDoctor(Long id, DoctorRequestDto doctorRequestDto) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Doctor with this id exists"));

        if(doctorRepository.existsByEmail(doctorRequestDto.getEmail())){
            throw new DuplicateResourceException("Doctor already exists with same email");
        }
        if(doctorRepository.existsByPhone(doctorRequestDto.getPhone())){
            throw new DuplicateResourceException("Doctor already exists with same phone");
        }

        modelMapper.map(doctorRequestDto, doctor);

        return modelMapper.map(doctorRepository.save(doctor), DoctorResponseDto.class);
    }

    /* Update Partial Doctor */
    @Override
    public DoctorResponseDto updatePartialDoctor(Long id, DoctorPatchRequestDto doctorPatchRequestDto){
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor does not exist"));

        if(doctorRepository.existsByEmail(doctorPatchRequestDto.getEmail())){
            throw new DuplicateResourceException("Doctor already exists with same email");
        }
        if(doctorRepository.existsByPhone(doctorPatchRequestDto.getPhone())){
            throw new DuplicateResourceException("Doctor already exists with same phone");
        }

        if (doctorPatchRequestDto.getName() != null && !doctorPatchRequestDto.getName().isEmpty()) {
            doctor.setName(doctorPatchRequestDto.getName());
        }if (doctorPatchRequestDto.getEmail() != null && !doctorPatchRequestDto.getEmail().isEmpty()){
            doctor.setEmail(doctorPatchRequestDto.getEmail());
        }if (doctorPatchRequestDto.getPhone() != null && !doctorPatchRequestDto.getPhone().isEmpty()){
            doctor.setPhone(doctorPatchRequestDto.getPhone());
        }if (doctorPatchRequestDto.getDob() != null && !doctorPatchRequestDto.getEmail().isEmpty()){
            doctor.setDob(doctorPatchRequestDto.getDob());
        }if (doctorPatchRequestDto.getGender() != null){
            doctor.setGender(doctorPatchRequestDto.getGender());
        }if (doctorPatchRequestDto.getAddress() != null){
            doctor.setAddress(doctorPatchRequestDto.getAddress());
        }if(doctorPatchRequestDto.getDepartmentId() != null){
            doctor.setDepartment();
        }
    }

    /* Delete Doctor */
}
