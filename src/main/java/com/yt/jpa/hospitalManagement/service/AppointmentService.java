package com.yt.jpa.hospitalManagement.service;

import com.yt.jpa.hospitalManagement.dto.request.AppointmentRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.AppointmentUpdateRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.AppointmentResponseDto;

import java.util.List;

public interface AppointmentService {
    /* All Appointments */
    List<AppointmentResponseDto> findAll();

    /* All Appointments of a Doctor */
    List<AppointmentResponseDto> findAllByDoctorId(Long doctorId);

    /* All Appointments of a Patient */
    List<AppointmentResponseDto> findAllByPatientId(Long patientId);

    /* Appointment by Appointment id */
    AppointmentResponseDto findById(Long id);

    /* Create Appointment (Status = PENDING) */
    AppointmentResponseDto createAppointment(AppointmentRequestDto appointmentRequestDto);

//    /* Doctor Accepts & Rejects Appointment (Update Status) */
//    AppointmentResponseDto acceptAppointment(Long doctorId, Long appointmentId);
//    AppointmentResponseDto rejectAppointment(Long doctorId, Long appointmentId);
//
//    /* Complete Appointment (Update Status) */
//    AppointmentResponseDto completeAppointment(Long doctorId, Long appointmentId);

//    Update Appointment
    AppointmentResponseDto updateAppointment(Long appointmentId, AppointmentUpdateRequestDto appointmentUpdateRequestDto);
}