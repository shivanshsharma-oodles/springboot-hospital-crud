package com.yt.jpa.hospitalManagement.service;

import com.yt.jpa.hospitalManagement.dto.request.BillRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.BillResponseDto;

import java.util.List;

public interface BillService {
    /* Get All Bills */
    List<BillResponseDto> findAllBills();

    /* Get Bill By id */
    BillResponseDto findBillById(Long id);

    /* Get all Bills of a patient */
    List<BillResponseDto> findBillsByPatientId(Long patientId);

    /* Get Bill By Appointment Id */
    BillResponseDto findBillByAppointmentId(Long appointmentId);

    /* Create Bill */
    BillResponseDto createBill(BillRequestDto billRequestDto);
}
