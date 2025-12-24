package com.yt.jpa.hospitalManagement.service;

import com.yt.jpa.hospitalManagement.dto.request.AppointmentRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.MedicalRecordRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.AppointmentResponseDto;
import com.yt.jpa.hospitalManagement.entity.User;
import com.yt.jpa.hospitalManagement.enums.AppointmentStatus;

import java.util.List;

public interface AppointmentService {
    /* All Appointments */
    List<AppointmentResponseDto> findAll(Long doctorId, Long patientId);

//    /* All Appointments of a Doctor */
//    List<AppointmentResponseDto> findAllByDoctorId(Long doctorId);
//
//    /* All Appointments of a Patient */
//    List<AppointmentResponseDto> findAllByPatientId(Long patientId);

    /* Appointment by Appointment id */
    AppointmentResponseDto findById(Long id);

    /* Create Appointment (Status = PENDING) */
    AppointmentResponseDto createAppointment(Long id, AppointmentRequestDto appointmentRequestDto);

//    Appointment
    AppointmentResponseDto completeAppointment(Long appointmentId, Long doctorId, MedicalRecordRequestDto medicalRecordRequestDto);

//    Update Appointment
    AppointmentResponseDto updateAppointment(Long doctorId, Long appointmentId, AppointmentStatus appointmentStatus);

//    Cancel Appointment
    void cancelAppointment(Long userId, Long appointmentId);

    List<AppointmentResponseDto> findMyAppointments(User user);
}