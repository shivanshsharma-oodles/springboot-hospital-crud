package com.yt.jpa.hospitalManagement.controller;

import com.yt.jpa.hospitalManagement.dto.request.MedicalRecordRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.MedicalRecordResponseDto;
import com.yt.jpa.hospitalManagement.service.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDto> getRecord(@PathVariable Long id) {
        return ResponseEntity.ok(medicalRecordService.findMedicalRecordById(id));
    }

//    @PostMapping
//    public ResponseEntity<MedicalRecordResponseDto> createRecord(
//            @Valid  @RequestBody MedicalRecordRequestDto dto) {
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(medicalRecordService.createMedicalRecord(dto));
//    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalRecordResponseDto>> getRecordsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(medicalRecordService.findAllByPatientId(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<MedicalRecordResponseDto>> getRecordsByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(medicalRecordService.findAllByDoctorId(doctorId));
    }
}