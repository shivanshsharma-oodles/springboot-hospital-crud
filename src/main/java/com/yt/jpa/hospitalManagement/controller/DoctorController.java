package com.yt.jpa.hospitalManagement.controller;

import com.yt.jpa.hospitalManagement.dto.request.patch.DoctorPatchRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.DoctorRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.DoctorResponseDto;
import com.yt.jpa.hospitalManagement.dto.response.publicDto.DoctorPublicDto;
import com.yt.jpa.hospitalManagement.entity.User;
import com.yt.jpa.hospitalManagement.service.DoctorService;
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
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

//    PUBLIC
    @GetMapping("")
    public ResponseEntity<List<DoctorResponseDto>> findAll() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorPublicDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok()
                .body(doctorService.getDoctorPubliclyById(id));
    }

//    ADMIN ONLY
    @GetMapping("/admin/{id}")
    public ResponseEntity<DoctorResponseDto> getDoctorByAdmin(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                doctorService.getDoctorsById(id)
        );
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

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteDoctor(
            @PathVariable Long id
    ) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

//    DOCTOR ONLY
    @GetMapping("/me")
    public ResponseEntity<DoctorResponseDto> findById() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok()
                .body(doctorService.getDoctorsById(user.getId()));
    }

    @PutMapping("")
    public ResponseEntity<DoctorResponseDto> updateDoctor(
            @Valid @RequestBody DoctorRequestDto doctorRequestDto
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity
                .ok(doctorService.updateDoctor(user.getId(), doctorRequestDto));
    }

    @PatchMapping("")
    public ResponseEntity<DoctorResponseDto> patchUpdateDoctor(
            @Valid @RequestBody DoctorPatchRequestDto doctorPatchRequestDto
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity
                .ok(doctorService.updatePartialDoctor(user.getId(), doctorPatchRequestDto));
    }
}