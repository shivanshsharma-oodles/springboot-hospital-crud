package com.yt.jpa.hospitalManagement.service.impl;

import com.yt.jpa.hospitalManagement.dto.request.PatientPatchRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.PatientRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.PatientResponseDto;
import com.yt.jpa.hospitalManagement.entity.Patient;
import com.yt.jpa.hospitalManagement.exception.DuplicateResourceException;
import com.yt.jpa.hospitalManagement.exception.ResourceNotFoundException;
import com.yt.jpa.hospitalManagement.repository.PatientRepository;
import com.yt.jpa.hospitalManagement.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    /* Get All Patients */
    @Override
    public List<PatientResponseDto> findAllPatients() {
        List<Patient> patients = patientRepository.findAll();

//        convert patient to patient response dto
        return patients.stream()
                .map(p -> modelMapper.map(p, PatientResponseDto.class))
                .toList();
    }

    /* Get Patients by Id */
    @Override
    public PatientResponseDto findPatientById(Long id) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Such Patient Exists."));
        return modelMapper.map(patient, PatientResponseDto.class);
    }

    /* Create Patient */
    @Override
    public PatientResponseDto createPatient(PatientRequestDto patientRequestDto) {
        if (patientRepository.existsByEmail(patientRequestDto.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }
        if (patientRepository.existsByPhone(patientRequestDto.getPhone())) {
            throw new DuplicateResourceException("Phone already exists");
        }

        Patient patient = modelMapper.map(patientRequestDto, Patient.class);
        patientRepository.save(patient);

        return modelMapper.map(patient, PatientResponseDto.class);
    }

    /* Update Patient */
    @Override
    public PatientResponseDto updatePatient(Long id, PatientRequestDto patientRequestDto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such patient exists."));

        if(patientRepository.existsByEmail(patientRequestDto.getEmail())){
            throw new DuplicateResourceException("Doctor already exists with same email");
        }
        if(patientRepository.existsByPhone(patientRequestDto.getPhone())){
            throw new DuplicateResourceException("Doctor already exists with same phone");
        }

//        Map data from dto to entity directly if patient exists.
        modelMapper.map(patientRequestDto, patient);

        return modelMapper.map(patientRepository.save(patient), PatientResponseDto.class);
    }

    /* Update Partial Patient */
    @Override
    public PatientResponseDto updatePartialPatient(Long id, PatientPatchRequestDto patientPatchRequestDto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such patient exists."));

        if (patientPatchRequestDto.getName() != null && !patientPatchRequestDto.getName().isEmpty()) {
            patient.setName(patientPatchRequestDto.getName());
        }
        if (patientPatchRequestDto.getEmail() != null && !patientPatchRequestDto.getEmail().isEmpty()) {
            patient.setEmail(patientPatchRequestDto.getEmail());
        }
        if (patientPatchRequestDto.getPhone() != null && !patientPatchRequestDto.getPhone().isEmpty()) {
            patient.setPhone(patientPatchRequestDto.getPhone());
        }
        if (patientPatchRequestDto.getDob() != null) {
            patient.setDob(patientPatchRequestDto.getDob());
        }
        if (patientPatchRequestDto.getGender() != null) {
            patient.setGender(patientPatchRequestDto.getGender());
        }
        if (patientPatchRequestDto.getAddress() != null) {
            patient.setAddress(patientPatchRequestDto.getAddress());
        }
        return modelMapper.map(patientRepository.save(patient), PatientResponseDto.class);
    }

    /* Delete Patient */
    @Override
    public Void deletePatient(Long id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("No Such Patient Exists.");
        }
        return null;
    }

}