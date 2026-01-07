package com.yt.jpa.hospitalManagement.controller;

import com.yt.jpa.hospitalManagement.dto.request.MedicalRecordRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.MedicalRecordResponseDto;
import com.yt.jpa.hospitalManagement.entity.User;
import com.yt.jpa.hospitalManagement.service.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;

    @GetMapping("/admin/{id}")
    public ResponseEntity<MedicalRecordResponseDto> getRecord(@PathVariable Long id) {
        return ResponseEntity.ok(medicalRecordService.findMedicalRecordById(id));
    }

//    Get Medical Records of all patients by patient_id
    @GetMapping("/admin/patient/{patientId}")
    public ResponseEntity<List<MedicalRecordResponseDto>> getRecordsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(medicalRecordService.findAllByPatientId(patientId));
    }

    //    Get Medical Records of all doctors by doctor_id
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<MedicalRecordResponseDto>> getRecordsByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(medicalRecordService.findAllByDoctorId(doctorId));
    }

//    Get Own Medical Record By id.
    @GetMapping("/get/{id}")
    public ResponseEntity<MedicalRecordResponseDto> getRecordById(@PathVariable Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(medicalRecordService.findMedicalRecordById(user.getId(), id));
    }

}