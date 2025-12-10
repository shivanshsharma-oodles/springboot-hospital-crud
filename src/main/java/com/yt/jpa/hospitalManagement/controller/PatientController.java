package com.yt.jpa.hospitalManagement.controller;

import com.yt.jpa.hospitalManagement.dto.request.PatientPatchRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.PatientRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.PatientResponseDto;
import com.yt.jpa.hospitalManagement.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    @GetMapping("")
    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {
        return ResponseEntity.ok(patientService.findAllPatients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDto> getPatientById(@PathVariable Long id) {
        return ResponseEntity
                .ok()
                .body(patientService.findPatientById(id));
    }

    @PostMapping("")
    public ResponseEntity<PatientResponseDto> createPatient(
            @Valid @RequestBody PatientRequestDto patientRequestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.createPatient(patientRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDto> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientRequestDto patientRequestDto
    ) {
        return ResponseEntity.ok(patientService.updatePatient(id, patientRequestDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PatientResponseDto> patchPatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientPatchRequestDto patientPatchRequestDto
    ) {
        return ResponseEntity
                .ok()
                .body(patientService.updatePartialPatient(id, patientPatchRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PatientResponseDto> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}