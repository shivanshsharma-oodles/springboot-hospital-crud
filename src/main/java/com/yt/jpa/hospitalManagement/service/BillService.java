package com.yt.jpa.hospitalManagement.service;

import com.yt.jpa.hospitalManagement.dto.request.BillRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.BillResponseDto;

import java.util.List;

public interface BillService {
    /* Get All Bills */
    List<BillResponseDto> findAllBills(Long patientId);

    /* Get Bill By id */
    BillResponseDto findBillById(Long id);

    /* Bills by Appointment id */
    BillResponseDto findBillByAppointmentIdForAdmin(Long appointmentId);

    /* Get all Bills of a patient */
    List<BillResponseDto> findBillsByPatientId(Long patientId);

    /* Get Bill By Appointment id */
    BillResponseDto findBillByAppointmentId(Long patientId, Long appointmentId);

    /* Create Bill */
    BillResponseDto createBill(Long doctorId, BillRequestDto billRequestDto);
}
