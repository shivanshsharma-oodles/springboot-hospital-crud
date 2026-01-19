package com.yt.jpa.hospitalManagement.service.impl;

import com.yt.jpa.hospitalManagement.dto.request.AppointmentRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.MedicalRecordRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.AppointmentResponseDto;
import com.yt.jpa.hospitalManagement.dto.response.MedicalRecordResponseDto;
import com.yt.jpa.hospitalManagement.entity.*;
import com.yt.jpa.hospitalManagement.enums.AppointmentStatus;
import com.yt.jpa.hospitalManagement.enums.Role;
import com.yt.jpa.hospitalManagement.exception.ResourceNotFoundException;
import com.yt.jpa.hospitalManagement.exception.UnauthorizedActionException;
import com.yt.jpa.hospitalManagement.repository.AppointmentRepository;
import com.yt.jpa.hospitalManagement.repository.DoctorRepository;
import com.yt.jpa.hospitalManagement.repository.DoctorSlotRepository;
import com.yt.jpa.hospitalManagement.repository.PatientRepository;
import com.yt.jpa.hospitalManagement.service.AppointmentService;
import com.yt.jpa.hospitalManagement.service.MedicalRecordService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorSlotRepository doctorSlotRepository;

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
    @Transactional
    public AppointmentResponseDto createAppointment(Long patientId, AppointmentRequestDto appointmentRequestDto){

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->  new ResourceNotFoundException("Patient Not Found"));

// PESSIMISTIC WRITE LOCK
        DoctorSlot slot = doctorSlotRepository.findByIdWithLock(appointmentRequestDto.getDoctorSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

        // Prevent double booking
        boolean blocked = appointmentRepository
                .existsByDoctorSlotAndAppointmentStatusNotIn(
                        slot,
                        List.of(AppointmentStatus.REJECTED, AppointmentStatus.CANCELLED)
                );

        if(slot.getDate().isBefore(today)
            || (slot.getDate().isEqual(today) && slot.getStartTime().isBefore(now.plusMinutes(15)))
        ){
            throw new IllegalStateException("Slot time already passed");

        }

        if (blocked) {
            throw new IllegalStateException("Slot already booked");
        }

        Appointment appointment = new Appointment();
        appointment.setDoctor(slot.getDoctor());
        appointment.setPatient(patient);
        appointment.setDoctorSlot(slot);
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
         MedicalRecordResponseDto record = medicalRecordService.createMedicalRecord(
                appointmentId,
                doctorId,
                appointment.getPatient().getId(),
                medicalRecordRequestDto
        );

        // Mark appointment completed
        appointment.setAppointmentStatus(AppointmentStatus.COMPLETED);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // 3. Map to DTO and MANUALLY set the record ID for this specific response
        // (Because the 'appointment' object in memory might not have refreshed the @OneToOne link yet)
        AppointmentResponseDto response = modelMapper.map(savedAppointment, AppointmentResponseDto.class);
        response.setMedicalRecordId(record.getId());

        return response;
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
    public void cancelAppointment(Long userId, Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("appointment not found"));

//        boolean isDoctor = doctorRepository.existsByIdAndStatusNot(userId, DoctorStatus.ARCHIVED);
//        boolean isPatient = patientRepository.existsById(userId);

        if(!appointment.getDoctor().getId().equals(userId)
                && !appointment.getPatient().getId().equals(userId)
        ){
            throw new UnauthorizedActionException("Unauthorized Action");
        }

        AppointmentStatus currentStatus = appointment.getAppointmentStatus();
        if (currentStatus == AppointmentStatus.CANCELLED
                || currentStatus == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Appointment cannot be cancelled");
        }

        // Mark appointment cancelled
        appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
//         return modelMapper.map(appointmentRepository.save(appointment), AppointmentResponseDto.class);
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
