package com.yt.jpa.hospitalManagement.controller;

import com.yt.jpa.hospitalManagement.dto.request.BillRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.BillResponseDto;
import com.yt.jpa.hospitalManagement.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bills")
@RequiredArgsConstructor
public class BillController {
    private final BillService billService;

    @GetMapping("")
    public ResponseEntity<List<BillResponseDto>> getAllBills() {
        return ResponseEntity
                .ok(billService.findAllBills());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillResponseDto> getBillById(@PathVariable Long id) {
        return ResponseEntity
                .ok(billService.findBillById(id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<BillResponseDto>> getBillsByPatientId(@PathVariable Long patientId) {
        return ResponseEntity
                .ok(billService.findBillsByPatientId(patientId));
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<BillResponseDto> getBillsByAppointmentId(@PathVariable Long appointmentId) {
        return ResponseEntity
                .ok(billService.findBillByAppointmentId(appointmentId));
    }

    @PostMapping("")
    public ResponseEntity<BillResponseDto> createBill(@RequestBody BillRequestDto billRequestDto) {
        return ResponseEntity
                .ok(billService.createBill(billRequestDto));
    }
}
