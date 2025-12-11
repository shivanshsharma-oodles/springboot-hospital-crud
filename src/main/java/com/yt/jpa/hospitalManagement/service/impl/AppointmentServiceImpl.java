package com.yt.jpa.hospitalManagement.service.impl;

import com.yt.jpa.hospitalManagement.dto.request.AppointmentRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.AppointmentUpdateRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.AppointmentResponseDto;
import com.yt.jpa.hospitalManagement.entity.Appointment;
import com.yt.jpa.hospitalManagement.entity.Doctor;
import com.yt.jpa.hospitalManagement.entity.Patient;
import com.yt.jpa.hospitalManagement.enums.AppointmentStatus;
import com.yt.jpa.hospitalManagement.enums.DoctorStatus;
import com.yt.jpa.hospitalManagement.exception.ResourceNotFoundException;
import com.yt.jpa.hospitalManagement.exception.UnauthorizedActionException;
import com.yt.jpa.hospitalManagement.repository.AppointmentRepository;
import com.yt.jpa.hospitalManagement.repository.DoctorRepository;
import com.yt.jpa.hospitalManagement.repository.PatientRepository;
import com.yt.jpa.hospitalManagement.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    /* Get All Appointments */
    @Override
    public List<AppointmentResponseDto> findAll(){
        List<Appointment> appointments = appointmentRepository.findAll();

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

    /* All Appointments of a doctor by doctorId */
    @Override
    public List<AppointmentResponseDto> findAllByDoctorId(Long doctorId){

        // Checking if Doctor is present or not
        doctorRepository.findByIdAndStatusNot(doctorId, DoctorStatus.ARCHIVED)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor Not Found"));

        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);

        return appointments.stream()
                .map(a -> modelMapper.map(a, AppointmentResponseDto.class))
                .toList();
    }

    /* All Appointments of a doctor by doctorId */
    @Override
    public List<AppointmentResponseDto> findAllByPatientId(Long patientId){
        if(patientRepository.findById(patientId).isEmpty()){
            throw new ResourceNotFoundException("Patient Not Found");
        }
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);

        return appointments.stream()
                .map(a -> modelMapper.map(a, AppointmentResponseDto.class))
                .toList();
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

    /* Update Appointment */
    @Override
    public AppointmentResponseDto updateAppointment(Long appointmentId, AppointmentUpdateRequestDto appointmentUpdateRequestDto){
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("appointment not found"));

        if(!appointment.getDoctor().getId().equals(appointmentUpdateRequestDto.getDoctorId())){
            throw new UnauthorizedActionException("Unauthorized Action");
        }

        AppointmentStatus currentStatus = appointment.getAppointmentStatus();
        if(currentStatus == AppointmentStatus.REJECTED || currentStatus == AppointmentStatus.COMPLETED){
            throw new RuntimeException("Completed/Rejected appointment cannot be changed");
        }
        appointment.setAppointmentStatus(appointmentUpdateRequestDto.getStatus());

        return modelMapper.map(appointmentRepository.save(appointment), AppointmentResponseDto.class);
    }

}
