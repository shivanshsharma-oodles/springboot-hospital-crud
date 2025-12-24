package com.yt.jpa.hospitalManagement.repository;

import com.yt.jpa.hospitalManagement.entity.Appointment;
import com.yt.jpa.hospitalManagement.entity.Doctor;
import com.yt.jpa.hospitalManagement.entity.DoctorSlot;
import com.yt.jpa.hospitalManagement.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    List<Appointment> findByDoctor_Id(Long id);
    List<Appointment> findByPatient_Id(Long id);
    List<Appointment> findByDoctor_IdAndPatient_Id(Long doctorId, Long patientId);
    boolean existsByDoctorSlotAndAppointmentStatusNot(DoctorSlot doctorSlot, AppointmentStatus appointmentStatus);

    @Query("SELECT a.doctorSlot.id FROM Appointment a WHERE a.doctor = :doctor AND a.appointmentStatus IN :statuses")
    List<Long> findDoctorSlotIdsByDoctorAndAppointmentStatusIn(
            @Param("doctor") Doctor doctor,
            @Param("statuses")List<AppointmentStatus> statuses
    );

}
