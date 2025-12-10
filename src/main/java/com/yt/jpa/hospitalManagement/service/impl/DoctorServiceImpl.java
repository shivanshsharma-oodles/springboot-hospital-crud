package com.yt.jpa.hospitalManagement.service.impl;

import com.yt.jpa.hospitalManagement.dto.request.DoctorPatchRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.DoctorRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.DoctorResponseDto;
import com.yt.jpa.hospitalManagement.entity.Department;
import com.yt.jpa.hospitalManagement.entity.Doctor;
import com.yt.jpa.hospitalManagement.enums.DoctorStatus;
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
        List<Doctor> doctors = doctorRepository.findByStatusNot(DoctorStatus.ARCHIVED);

//        Convert Doctor to Doctor Response Dto
        return doctors.stream()
                .map(d -> modelMapper.map(d, DoctorResponseDto.class))
                .toList();
    }

    /* Get Doctor By id */
    @Override
    public DoctorResponseDto getDoctorsById(Long id) {
        Doctor doctor = doctorRepository.findByIdAndStatusNot(id, DoctorStatus.ARCHIVED)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor does not exist"));

        return modelMapper.map(doctor, DoctorResponseDto.class);
    }

    /* Create Doctor */
    @Override
    public DoctorResponseDto createDoctor(DoctorRequestDto doctorRequestDto) {
        if(doctorRepository.existsByEmailAndStatusNot(doctorRequestDto.getEmail(), DoctorStatus.ARCHIVED)){
            throw new DuplicateResourceException("Doctor already exists with same email");
        }
        if(doctorRepository.existsByPhoneAndStatusNot(doctorRequestDto.getPhone(), DoctorStatus.ARCHIVED)){
            throw new DuplicateResourceException("Doctor already exists with same phone");
        }

        // Load Dept from DB
        Department department = departmentRepository.findById(doctorRequestDto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department does not exist"));

        Doctor doctor = modelMapper.map(doctorRequestDto, Doctor.class);
        doctor.setDepartment(department);

        return modelMapper.map(doctorRepository.save(doctor), DoctorResponseDto.class);
    }

    /* Update Doctor */
    @Override
    public DoctorResponseDto updateDoctor(Long id, DoctorRequestDto doctorRequestDto) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Doctor with this id exists"));

        if(doctorRepository.existsByEmailAndStatusNotAndIdNot(doctorRequestDto.getEmail(), DoctorStatus.ARCHIVED, id)){
            throw new DuplicateResourceException("Doctor already exists with same email");
        }
        if(doctorRepository.existsByPhoneAndStatusNotAndIdNot(doctorRequestDto.getPhone(), DoctorStatus.ARCHIVED, id)){
            throw new DuplicateResourceException("Doctor already exists with same phone");
        }

        Department department = departmentRepository.findById(doctorRequestDto.getDepartmentId())
                        .orElseThrow(() -> new ResourceNotFoundException("Department does not exist"));

        modelMapper.map(doctorRequestDto, doctor);
        doctor.setDepartment(department);

        return modelMapper.map(doctorRepository.save(doctor), DoctorResponseDto.class);
    }

    /* Update Partial Doctor */
    @Override
    public DoctorResponseDto updatePartialDoctor(Long id, DoctorPatchRequestDto doctorPatchRequestDto){
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor does not exist"));

        if (doctorPatchRequestDto.getName() != null && !doctorPatchRequestDto.getName().isEmpty()) {
            doctor.setName(doctorPatchRequestDto.getName());
        }if (doctorPatchRequestDto.getEmail() != null && !doctorPatchRequestDto.getEmail().isEmpty()){
            if(doctorRepository.existsByEmailAndStatusNot(doctorPatchRequestDto.getEmail(), DoctorStatus.ARCHIVED)){
                throw new DuplicateResourceException("Doctor already exists with same email");
            }
            doctor.setEmail(doctorPatchRequestDto.getEmail());
        }if (doctorPatchRequestDto.getPhone() != null && !doctorPatchRequestDto.getPhone().isEmpty()){
            if(doctorRepository.existsByPhoneAndStatusNot(doctorPatchRequestDto.getPhone(), DoctorStatus.ARCHIVED)){
                throw new DuplicateResourceException("Doctor already exists with same phone");
            }
            doctor.setPhone(doctorPatchRequestDto.getPhone());
        }if (doctorPatchRequestDto.getDob() != null){
            doctor.setDob(doctorPatchRequestDto.getDob());
        }if (doctorPatchRequestDto.getGender() != null){
            doctor.setGender(doctorPatchRequestDto.getGender());
        }if (doctorPatchRequestDto.getAddress() != null){
            doctor.setAddress(doctorPatchRequestDto.getAddress());
        }if(doctorPatchRequestDto.getDepartmentId() != null){
            Department department = departmentRepository.findById(doctorPatchRequestDto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department does not exist"));
            doctor.setDepartment(department);
        }

        return  modelMapper.map(doctorRepository.save(doctor), DoctorResponseDto.class);
    }

    /* Delete Doctor */
    public void deleteDoctor(Long id){
        Doctor doctor =  doctorRepository.findByIdAndStatusNot(id, DoctorStatus.ARCHIVED)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor does not exist"));

        doctor.setStatus(DoctorStatus.ARCHIVED);
        doctorRepository.save(doctor);
    }
}
