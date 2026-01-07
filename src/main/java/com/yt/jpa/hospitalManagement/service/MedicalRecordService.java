package com.yt.jpa.hospitalManagement.service;

import com.yt.jpa.hospitalManagement.dto.request.MedicalRecordRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.MedicalRecordResponseDto;
import com.yt.jpa.hospitalManagement.dto.summary.MedicalRecordSummaryDto;

import java.util.List;

public interface MedicalRecordService {

    /* Get all medical records of a patient */
    List<MedicalRecordResponseDto> findAllByPatientId(Long patientId);

    /* Get all medical records of a Doctor */
    List<MedicalRecordResponseDto> findAllByDoctorId(Long doctorId);

    /* Get Medical Record by id */
    MedicalRecordResponseDto findMedicalRecordById(Long id);

    /* Get Medical Record of Own by id */
    MedicalRecordResponseDto findMedicalRecordById(Long userId, Long id);

    /* Create Medical Record */
    MedicalRecordResponseDto createMedicalRecord(Long appointmentId, Long DoctorId, Long patientId, MedicalRecordRequestDto medicalRecordRequestDto);

    List<MedicalRecordSummaryDto> findAllByUserId(Long userId);
}
