package com.yt.jpa.hospitalManagement.controller;

import com.yt.jpa.hospitalManagement.dto.request.AppointmentRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.AppointmentUpdateRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.AppointmentResponseDto;
import com.yt.jpa.hospitalManagement.dto.response.DepartmentResponseDto;
import com.yt.jpa.hospitalManagement.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping("")
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointments() {
        return ResponseEntity
                .ok(appointmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDto> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity
                .ok(appointmentService.findById(id));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentResponseDto>> getAppointmentByDoctorId(@PathVariable Long doctorId) {
        return ResponseEntity
                .ok(appointmentService.findAllByDoctorId(doctorId));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponseDto>> getAppointmentByPatientId(@PathVariable Long patientId) {
        return ResponseEntity
                .ok(appointmentService.findAllByPatientId(patientId));
    }

    @PostMapping("")
    public ResponseEntity<AppointmentResponseDto> createAppointment(@RequestBody AppointmentRequestDto appointmentRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED).body(appointmentService.createAppointment(appointmentRequestDto));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AppointmentResponseDto> updateAppointment(
            @PathVariable Long id,
            @RequestBody AppointmentUpdateRequestDto appointmentUpdateRequestDto
    ) {
        return ResponseEntity
                .ok(appointmentService.updateAppointment(id ,appointmentUpdateRequestDto));
    }
}
