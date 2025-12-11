package com.yt.jpa.hospitalManagement.service.impl;

import com.yt.jpa.hospitalManagement.dto.request.MedicalRecordRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.MedicalRecordResponseDto;
import com.yt.jpa.hospitalManagement.entity.Doctor;
import com.yt.jpa.hospitalManagement.entity.MedicalRecord;
import com.yt.jpa.hospitalManagement.entity.Patient;
import com.yt.jpa.hospitalManagement.exception.ResourceNotFoundException;
import com.yt.jpa.hospitalManagement.repository.DoctorRepository;
import com.yt.jpa.hospitalManagement.repository.MedicalRecordRepository;
import com.yt.jpa.hospitalManagement.repository.PatientRepository;
import com.yt.jpa.hospitalManagement.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    /* Get all medical records of a patient */
    @Override
    public List<MedicalRecordResponseDto> findAllByPatientId(Long patientId){

//        Patient Id Check
        patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        List<MedicalRecord> medicalRecords = medicalRecordRepository.findByPatientId(patientId);

        return medicalRecords.stream()
                .map(m -> modelMapper.map(m, MedicalRecordResponseDto.class))
                .toList();
    }

    /* Get all medical records of a Doctor */
    @Override
    public List<MedicalRecordResponseDto> findAllByDoctorId(Long doctorId){
        doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        List<MedicalRecord> medicalRecords = medicalRecordRepository.findByDoctorId(doctorId);

        return medicalRecords.stream()
                .map(m -> modelMapper.map(m, MedicalRecordResponseDto.class))
                .toList();
    }

    /* Get Medical Record by id */
    @Override
    public MedicalRecordResponseDto findMedicalRecordById(Long id){
        MedicalRecord med = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found"));

        return modelMapper.map(med, MedicalRecordResponseDto.class);
    }

    /* Create Medical Record */
    @Override
    public MedicalRecordResponseDto createMedicalRecord(MedicalRecordRequestDto medicalRecordRequestDto){
        Doctor doctor = doctorRepository.findById(medicalRecordRequestDto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        Patient patient = patientRepository.findById(medicalRecordRequestDto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        MedicalRecord record = new MedicalRecord();
        record.setDoctor(doctor);
        record.setPatient(patient);

        return modelMapper.map(medicalRecordRepository.save(record), MedicalRecordResponseDto.class);
    }

}
