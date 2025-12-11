package com.yt.jpa.hospitalManagement.service.impl;

import com.yt.jpa.hospitalManagement.dto.request.BillRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.BillResponseDto;
import com.yt.jpa.hospitalManagement.entity.Appointment;
import com.yt.jpa.hospitalManagement.entity.Bill;
import com.yt.jpa.hospitalManagement.entity.Patient;
import com.yt.jpa.hospitalManagement.exception.DuplicateResourceException;
import com.yt.jpa.hospitalManagement.exception.ResourceNotFoundException;
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
    public List<BillResponseDto> findAllBills(){
        List<Bill> bills = billRepository.findAll();

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

    /* Get all Bills of a patient */
    @Override
    public List<BillResponseDto> findBillsByPatientId(Long patientId){
        patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        List<Bill> bills = billRepository.findAllBillsByPatientId(patientId);

        return bills.stream()
                .map(b -> modelMapper.map(b, BillResponseDto.class))
                .toList();
    }

    /* Find Bill By Appointment Id */
    @Override
    public BillResponseDto findBillByAppointmentId(Long appointmentId){
        appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        Bill bill = billRepository.findBillByAppointmentId(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found"));

        return modelMapper.map(bill, BillResponseDto.class);
    }

    /* Create Bill */
    @Override
    public BillResponseDto createBill(BillRequestDto billRequestDto){
        Appointment appointment = appointmentRepository.findById(billRequestDto.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        Patient patient = patientRepository.findById(billRequestDto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

//      Prevent duplicate bill for same patient
        if(billRepository.findBillByAppointmentId(billRequestDto.getAppointmentId()).isPresent()){
            throw new DuplicateResourceException("Bill already exists for this appointment Id");
        }

        Bill bill = modelMapper.map(billRequestDto, Bill.class);
        bill.setAppointment(appointment);
        bill.setPatient(patient);

        return modelMapper.map(billRepository.save(bill), BillResponseDto.class);
    }
}
