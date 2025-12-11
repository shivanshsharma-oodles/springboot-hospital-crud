package com.yt.jpa.hospitalManagement.repository;

import com.yt.jpa.hospitalManagement.dto.response.BillResponseDto;
import com.yt.jpa.hospitalManagement.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill,Long> {
    List<Bill> findAllBillsByPatientId(Long patientId);
    Optional<Bill> findBillByAppointmentId(Long appointmentId);
}
