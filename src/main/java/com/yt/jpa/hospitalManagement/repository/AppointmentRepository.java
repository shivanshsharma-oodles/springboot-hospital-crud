package com.yt.jpa.hospitalManagement.repository;

import com.yt.jpa.hospitalManagement.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    List<Appointment> findByDoctorId(Long id);
    List<Appointment> findByPatientId(Long id);

    List<Appointment> findByPatientIdOrDoctorId(Long id);

    List<Appointment> findByDoctorIdAndPatientId(Long doctorId, Long patientId);
}
