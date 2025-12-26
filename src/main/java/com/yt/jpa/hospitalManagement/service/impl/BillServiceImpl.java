package com.yt.jpa.hospitalManagement.service.impl;

import com.yt.jpa.hospitalManagement.dto.request.BillRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.BillResponseDto;
import com.yt.jpa.hospitalManagement.entity.Appointment;
import com.yt.jpa.hospitalManagement.entity.Bill;
import com.yt.jpa.hospitalManagement.enums.AppointmentStatus;
import com.yt.jpa.hospitalManagement.exception.DuplicateResourceException;
import com.yt.jpa.hospitalManagement.exception.ResourceNotFoundException;
import com.yt.jpa.hospitalManagement.exception.UnauthorizedActionException;
import com.yt.jpa.hospitalManagement.repository.AppointmentRepository;
import com.yt.jpa.hospitalManagement.repository.BillRepository;
import com.yt.jpa.hospitalManagement.repository.PatientRepository;
import com.yt.jpa.hospitalManagement.service.BillService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    /* Get All Bills */
    @Override
    public List<BillResponseDto> findAllBills(Long patientId) {
        List<Bill> bills;
        if (patientId != null) {
            bills = billRepository
                    .findAllByPatientId(patientId);
        }
        else {
            // No filters, so all bills
            bills = billRepository.findAll();
        }
        return bills.stream()
                .map(b -> modelMapper.map(b, BillResponseDto.class))
                .toList();
    }

    /* Get Bill By id */
    @Override
    public BillResponseDto findBillById(Long id){
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found"));

        return modelMapper.map(bill, BillResponseDto.class);
    }

    @Override
    public BillResponseDto findBillByAppointmentIdForAdmin(Long appointmentId) {

        appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        Bill bill = billRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found"));

        return modelMapper.map(bill, BillResponseDto.class);
    }

    /* Get all Bills of a patient */
    @Override
    public List<BillResponseDto> findBillsByPatientId(Long patientId){
        patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        List<Bill> bills = billRepository.findAllByPatientId(patientId);

        return bills.stream()
                .map(b -> modelMapper.map(b, BillResponseDto.class))
                .toList();
    }

    /* Find Bill By Appointment Id */
    @Override
    public BillResponseDto findBillByAppointmentId(Long patientId, Long appointmentId){
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        // Patient can only view own appointment bill
        if(!appointment.getPatient().getId().equals(patientId)){
            throw new UnauthorizedActionException("You are not allowed to access this bill");
        }

        Bill bill = billRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found"));

        return modelMapper.map(bill, BillResponseDto.class);
    }

    /* Create Bill */
    @Override
    public BillResponseDto createBill(Long doctorId, Long appointmentId, BillRequestDto billRequestDto){

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (!appointment.getDoctor().getId().equals(doctorId)) {
            throw new UnauthorizedActionException(
                    "Doctor cannot bill for this appointment"
            );
        }

//        Generate bill for completed appointments only.
        if(appointment.getAppointmentStatus() != AppointmentStatus.COMPLETED){
            throw new RuntimeException("Can not generate bill for pending / scheduled / rejected appointments");
        }

//      Prevent duplicate bill
        if(billRepository.findByAppointmentId(appointmentId).isPresent()){
            throw new DuplicateResourceException("Bill already exists for this appointment Id");
        }

        Bill bill = new Bill();
        bill.setAmount(billRequestDto.getAmount());
        bill.setAppointment(appointment);
        bill.setPatient(appointment.getPatient());

        return modelMapper.map(billRepository.save(bill), BillResponseDto.class);
    }
}