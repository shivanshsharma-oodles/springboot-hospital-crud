package com.yt.jpa.hospitalManagement.service.impl;

import com.yt.jpa.hospitalManagement.dto.request.AppointmentRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.MedicalRecordRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.AppointmentResponseDto;
import com.yt.jpa.hospitalManagement.entity.Appointment;
import com.yt.jpa.hospitalManagement.entity.Doctor;
import com.yt.jpa.hospitalManagement.entity.Patient;
import com.yt.jpa.hospitalManagement.entity.User;
import com.yt.jpa.hospitalManagement.enums.AppointmentStatus;
import com.yt.jpa.hospitalManagement.enums.DoctorStatus;
import com.yt.jpa.hospitalManagement.enums.Role;
import com.yt.jpa.hospitalManagement.exception.ResourceNotFoundException;
import com.yt.jpa.hospitalManagement.exception.UnauthorizedActionException;
import com.yt.jpa.hospitalManagement.repository.AppointmentRepository;
import com.yt.jpa.hospitalManagement.repository.DoctorRepository;
import com.yt.jpa.hospitalManagement.repository.PatientRepository;
import com.yt.jpa.hospitalManagement.service.AppointmentService;
import com.yt.jpa.hospitalManagement.service.MedicalRecordService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    private final MedicalRecordService medicalRecordService;

    private final ModelMapper modelMapper;

    /* Get All Appointments */
    @Override
    public List<AppointmentResponseDto> findAll(Long patientId, Long doctorId) {
        List<Appointment> appointments;

        if (doctorId != null && patientId != null) {
            // Rare case (admin advanced filter)
            appointments = appointmentRepository
                    .findByDoctor_IdAndPatient_Id(doctorId, patientId);
        }
        else if (doctorId != null) {
            appointments = appointmentRepository
                    .findByDoctor_Id(doctorId);
        }
        else if (patientId != null) {
            appointments = appointmentRepository
                    .findByPatient_Id(patientId);
        }
        else {
            // No filters, so all appointments
            appointments = appointmentRepository.findAll();
        }

        return appointments.stream()
                .map(a -> modelMapper.map(a, AppointmentResponseDto.class))
                .toList();
    }

    /* Get Appointment By id */
    @Override
    public AppointmentResponseDto findById(Long id){
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("appointment not found"));

        return modelMapper.map(appointment, AppointmentResponseDto.class);
    }


    /* Create Appointment */
    @Override
    public AppointmentResponseDto createAppointment(AppointmentRequestDto appointmentRequestDto){
        Doctor doctor = doctorRepository.findByIdAndStatusNot(appointmentRequestDto.getDoctorId(), DoctorStatus.ARCHIVED)
                .orElseThrow(() ->  new ResourceNotFoundException("Doctor Not Found"));

        Patient patient = patientRepository.findById(appointmentRequestDto.getPatientId())
                .orElseThrow(() ->  new ResourceNotFoundException("Patient Not Found"));

//      appointment object
        Appointment appointment = new Appointment();

//        Set Doctor & Patient from their respective ids
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);

//        Set Appointment status
        appointment.setAppointmentStatus(AppointmentStatus.PENDING);

        return modelMapper.map(appointmentRepository.save(appointment), AppointmentResponseDto.class);
    }

    /* Complete Appointment */
    @Override
    @Transactional
    public AppointmentResponseDto completeAppointment(Long appointmentId, Long doctorId, MedicalRecordRequestDto medicalRecordRequestDto){
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("appointment not found"));

        if(!appointment.getDoctor().getId().equals(doctorId)){
            throw new UnauthorizedActionException("Unauthorized Action");
        }

        AppointmentStatus currentStatus = appointment.getAppointmentStatus();
        if(currentStatus == AppointmentStatus.REJECTED || currentStatus == AppointmentStatus.COMPLETED){
            throw new RuntimeException("Completed/Rejected appointment cannot be changed");
        }

        // Create medical record
        medicalRecordService.createMedicalRecord(
                appointmentId,
                doctorId,
                appointment.getPatient().getId(),
                medicalRecordRequestDto
        );

        // Mark appointment completed
        appointment.setAppointmentStatus(AppointmentStatus.COMPLETED);

        return modelMapper.map(appointmentRepository.save(appointment), AppointmentResponseDto.class);
    }


    /* Update Appointment */
    @Override
    public AppointmentResponseDto updateAppointment(Long doctorId, Long appointmentId, AppointmentStatus appointmentStatus){
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("appointment not found"));

        if(!appointment.getDoctor().getId().equals(doctorId)){
            throw new UnauthorizedActionException("Unauthorized Action");
        }

        AppointmentStatus currentStatus = appointment.getAppointmentStatus();
        if(currentStatus == AppointmentStatus.REJECTED || currentStatus == AppointmentStatus.COMPLETED){
            throw new RuntimeException("Completed/Rejected appointment cannot be changed");
        }

        if( appointmentStatus == AppointmentStatus.COMPLETED){
            throw new UnauthorizedActionException("Can Not Complete Appointment");
        }

        appointment.setAppointmentStatus(appointmentStatus);

        return modelMapper.map(appointmentRepository.save(appointment), AppointmentResponseDto.class);
    }

    @Override
    public List<AppointmentResponseDto> findMyAppointments(User user) {
        List<Appointment> appointments;
        if (user.getRoles().contains(Role.DOCTOR)) {
            Long doctorId = user.getId(); // MapsId → safe
            appointments = appointmentRepository.findByDoctor_Id(doctorId);

        } else if (user.getRoles().contains(Role.PATIENT)) {
            Long patientId = user.getId();
            appointments = appointmentRepository.findByPatient_Id(patientId);

        } else {
            throw new AccessDeniedException("Invalid role");
        }
        return appointments.stream()
                .map(a -> modelMapper.map(a, AppointmentResponseDto.class))
                .toList();
    }

}
