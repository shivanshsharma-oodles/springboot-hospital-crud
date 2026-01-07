package com.yt.jpa.hospitalManagement.service.impl;

import com.yt.jpa.hospitalManagement.dto.request.MedicalRecordRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.MedicalRecordResponseDto;
import com.yt.jpa.hospitalManagement.dto.summary.MedicalRecordSummaryDto;
import com.yt.jpa.hospitalManagement.entity.Appointment;
import com.yt.jpa.hospitalManagement.entity.Doctor;
import com.yt.jpa.hospitalManagement.entity.MedicalRecord;
import com.yt.jpa.hospitalManagement.entity.Patient;
import com.yt.jpa.hospitalManagement.exception.DuplicateResourceException;
import com.yt.jpa.hospitalManagement.exception.ResourceNotFoundException;
import com.yt.jpa.hospitalManagement.exception.UnauthorizedActionException;
import com.yt.jpa.hospitalManagement.repository.AppointmentRepository;
import com.yt.jpa.hospitalManagement.repository.DoctorRepository;
import com.yt.jpa.hospitalManagement.repository.MedicalRecordRepository;
import com.yt.jpa.hospitalManagement.repository.PatientRepository;
import com.yt.jpa.hospitalManagement.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final ModelMapper modelMapper;

    /* Get all medical records of a patient */
    @Override
    public List<MedicalRecordResponseDto> findAllByPatientId(Long patientId){

//        Patient Id Check
        patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        List<MedicalRecord> medicalRecords = medicalRecordRepository.findByPatientId(patientId);

        return medicalRecords.stream()
                .map(m -> modelMapper.map(m, MedicalRecordResponseDto.class))
                .toList();
    }

    /* Get all medical records of a Doctor */
    @Override
    public List<MedicalRecordResponseDto> findAllByDoctorId(Long doctorId){
        doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        List<MedicalRecord> medicalRecords = medicalRecordRepository.findByDoctorId(doctorId);

        return medicalRecords.stream()
                .map(m -> modelMapper.map(m, MedicalRecordResponseDto.class))
                .toList();
    }

    /* Get Medical Record by id */
    @Override
    public MedicalRecordResponseDto findMedicalRecordById(Long id){
        MedicalRecord med = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found"));

        return modelMapper.map(med, MedicalRecordResponseDto.class);
    }

//    Get Medical Record of own by id
    @Override
    public MedicalRecordResponseDto findMedicalRecordById(Long userId, Long id) {
        MedicalRecord med = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found"));

        boolean isDoctor = med.getDoctor().getId().equals(userId);
        boolean isPatient = med.getPatient().getId().equals(userId);

        if (!isDoctor && !isPatient) {
            throw new UnauthorizedActionException("You can not access this record.");
        }

        return modelMapper.map(med, MedicalRecordResponseDto.class);
    }

    /* Create Medical Record */
    @Override
    public MedicalRecordResponseDto createMedicalRecord(Long appointmentId, Long doctorId, Long patientId, MedicalRecordRequestDto dto){
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));


        if(!appointment.getPatient().getId().equals(patient.getId())){
            throw new RuntimeException("This appointment does not belong to this patient");
        }
        if(medicalRecordRepository.findByAppointmentId(appointmentId).isPresent()){
            throw new DuplicateResourceException("Medical record already exists for this appointment");
        }

        MedicalRecord record = new MedicalRecord();
        record.setDoctor(doctor);
        record.setPatient(patient);
        record.setAppointment(appointment);

        record.setSymptoms(dto.getSymptoms());
        record.setDiagnosis(dto.getDiagnosis());
        record.setPrescription(dto.getPrescription());
        record.setTreatment(dto.getTreatment());
        record.setFollowUpDate(dto.getFollowUpDate());
        record.setTemperature(dto.getTemperature());
        record.setPulse(dto.getPulse());
        record.setBpSystolic(dto.getBpSystolic());
        record.setBpDiastolic(dto.getBpDiastolic());

        return modelMapper.map(medicalRecordRepository.save(record), MedicalRecordResponseDto.class);
    }

    @Override
    public List<MedicalRecordSummaryDto> findAllByUserId(Long userId) {
        // 1. Check existence and roles
        boolean isDoctor = doctorRepository.existsById(userId);
        boolean isPatient = patientRepository.existsById(userId);

        if (!isDoctor && !isPatient) {
            throw new UnauthorizedActionException("You cannot access these records.");
        }

        List<MedicalRecord> medicalRecords;

        // 2. Determine which records to fetch
        // Note: If someone is BOTH, we default to Patient view
        if (isPatient) {
            // Patients see records where they are the recipient
            medicalRecords = medicalRecordRepository.findByPatientId(userId);
        } else {
            // Doctors see records they have written
            medicalRecords = medicalRecordRepository.findByDoctorId(userId);
        }

        // 3. Map and return
        return medicalRecords.stream()
                .map(m -> modelMapper.map(m, MedicalRecordSummaryDto.class))
                .toList();
    }
}
