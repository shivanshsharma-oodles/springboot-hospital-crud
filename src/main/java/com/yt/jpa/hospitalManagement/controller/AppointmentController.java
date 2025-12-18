package com.yt.jpa.hospitalManagement.controller;

import com.yt.jpa.hospitalManagement.dto.request.AppointmentRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.MedicalRecordRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.AppointmentResponseDto;
import com.yt.jpa.hospitalManagement.entity.User;
import com.yt.jpa.hospitalManagement.enums.AppointmentStatus;
import com.yt.jpa.hospitalManagement.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;
    /* ---------------- ADMIN ---------------- */

    // ADMIN: view all
    @GetMapping("/admin")
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointments(
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Long patientId
    ) {
        return ResponseEntity.ok(
                appointmentService.findAll(doctorId, patientId)
        );
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<AppointmentResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.findById(id));
    }

    /* ---------------- DOCTOR / PATIENT ---------------- */

    // DOCTOR or PATIENT: own appointments
    @GetMapping("/me")
    public ResponseEntity<List<AppointmentResponseDto>> myAppointments() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(
                appointmentService.findMyAppointments(user.getId())
        );
    }

    @PostMapping("")
    public ResponseEntity<AppointmentResponseDto> create(
            @RequestBody AppointmentRequestDto dto
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        dto.setPatientId(user.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(appointmentService.createAppointment(dto));
    }

    /* ---------------- DOCTOR ---------------- */

    // DOCTOR: update appointment status
    @PutMapping("/{id}/status")
    public ResponseEntity<AppointmentResponseDto> updateAppointment(
            @PathVariable Long id,
            @RequestBody AppointmentStatus appointmentStatus
            ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity
                .ok(appointmentService.updateAppointment(id, user.getId(), appointmentStatus));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<AppointmentResponseDto> updateAppointment(
            @PathVariable Long id,
            @RequestBody MedicalRecordRequestDto medicalRecordRequestDto
            ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity
                .ok(appointmentService.completeAppointment(id, user.getId(), medicalRecordRequestDto ));
    }
}