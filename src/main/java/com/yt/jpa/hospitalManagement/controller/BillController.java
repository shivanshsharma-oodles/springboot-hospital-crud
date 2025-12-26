package com.yt.jpa.hospitalManagement.controller;

import com.yt.jpa.hospitalManagement.dto.request.BillRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.BillResponseDto;
import com.yt.jpa.hospitalManagement.entity.User;
import com.yt.jpa.hospitalManagement.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bills")
@RequiredArgsConstructor
public class BillController {
    private final BillService billService;

    /* ---------------- ADMIN ---------------- */

    // ADMIN: view all
    @GetMapping("/admin")
    public ResponseEntity<List<BillResponseDto>> getAllBills(
            @RequestParam(required = false) Long patientId
            ) {
        return ResponseEntity
                .ok(billService.findAllBills(patientId));
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<BillResponseDto> getBillById(@PathVariable Long id) {
        return ResponseEntity
                .ok(billService.findBillById(id));
    }

    // ADMIN: bill by appointment id
    @GetMapping("/admin/appointment/{appointmentId}")
    public ResponseEntity<BillResponseDto> getBillByAppointmentIdForAdmin(
            @PathVariable Long appointmentId
    ) {
        return ResponseEntity.ok(
                billService.findBillByAppointmentIdForAdmin(appointmentId)
        );
    }

    /* ---------------- PATIENT ---------------- */

    // PATIENT: own bills
    @GetMapping("/me")
    public ResponseEntity<List<BillResponseDto>> myBills() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity
                .ok(billService.findBillsByPatientId(user.getId()));
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<BillResponseDto> getBillsByAppointmentId(
            @PathVariable Long appointmentId
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity
                .ok(billService.findBillByAppointmentId(user.getId(), appointmentId));
    }

    /* ---------------- DOCTOR ---------------- */

// Doctor: Create Bill
    @PostMapping("/{appointmentId}")
    public ResponseEntity<BillResponseDto> createBill(
            @PathVariable Long appointmentId,
            @RequestBody BillRequestDto billRequestDto
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity
                .ok(billService.createBill(user.getId(), appointmentId, billRequestDto));
    }
}
