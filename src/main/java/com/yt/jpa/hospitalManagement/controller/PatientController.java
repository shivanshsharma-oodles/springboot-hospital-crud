package com.yt.jpa.hospitalManagement.controller;

import com.yt.jpa.hospitalManagement.dto.request.patch.PatientPatchRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.PatientRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.PatientResponseDto;
import com.yt.jpa.hospitalManagement.entity.User;
import com.yt.jpa.hospitalManagement.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    /* ADMIN ONLY */
    @GetMapping("/admin")
    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {
        return ResponseEntity.ok(patientService.findAllPatients());
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<PatientResponseDto> getPatientByAdmin(@PathVariable Long id) {
        return ResponseEntity
                .ok()
                .body(patientService.findPatientById(id));
    }

    /* PATIENT ONLY */
    @GetMapping("/me")
    public ResponseEntity<PatientResponseDto> getPatientById() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity
                .ok()
                .body(patientService.findPatientById(user.getId()));
    }

    @PostMapping("")
    public ResponseEntity<PatientResponseDto> createPatient(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PatientRequestDto patientRequestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.createPatient(userDetails.getUsername(), patientRequestDto));
    }

    @PutMapping("")
    public ResponseEntity<PatientResponseDto> updatePatient(
            @Valid @RequestBody PatientRequestDto patientRequestDto
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(patientService.updatePatient(user.getId(), patientRequestDto));
    }

    @PatchMapping("")
    public ResponseEntity<PatientResponseDto> patchPatient(
            @Valid @RequestBody PatientPatchRequestDto patientPatchRequestDto
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity
                .ok()
                .body(patientService.updatePartialPatient(user.getId(), patientPatchRequestDto));
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<PatientResponseDto> deletePatient(@PathVariable Long id) {
//        patientService.deletePatient(id);
//        return ResponseEntity.noContent().build();
//    }
}