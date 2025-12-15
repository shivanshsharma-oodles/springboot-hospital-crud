package com.yt.jpa.hospitalManagement.controller;

import com.yt.jpa.hospitalManagement.dto.request.patch.DoctorPatchRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.DoctorRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.DoctorResponseDto;
import com.yt.jpa.hospitalManagement.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping("")
    public ResponseEntity<List<DoctorResponseDto>> findAll() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok()
                .body(doctorService.getDoctorsById(id));
    }

    @PostMapping("")
    public ResponseEntity<DoctorResponseDto> createDoctor(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody DoctorRequestDto doctorRequestDto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(doctorService.createDoctor(userDetails.getUsername(), doctorRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponseDto> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody DoctorRequestDto doctorRequestDto
    ) {
       return ResponseEntity
               .ok(doctorService.updateDoctor(id, doctorRequestDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DoctorResponseDto> patchUpdateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody DoctorPatchRequestDto  doctorPatchRequestDto
            ){
        return ResponseEntity
                .ok(doctorService.updatePartialDoctor(id, doctorPatchRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(
            @PathVariable Long id
    ) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
