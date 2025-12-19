package com.yt.jpa.hospitalManagement.repository;

import com.yt.jpa.hospitalManagement.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    List<Appointment> findByDoctor_Id(Long id);
    List<Appointment> findByPatient_Id(Long id);
    List<Appointment> findByDoctor_IdAndPatient_Id(Long doctorId, Long patientId);
}
