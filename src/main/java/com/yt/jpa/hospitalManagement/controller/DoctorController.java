package com.yt.jpa.hospitalManagement.controller;

import com.yt.jpa.hospitalManagement.dto.request.AdminCreateDoctorRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.DoctorSlotRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.patch.DoctorPatchRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.DoctorRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.DoctorResponseDto;
import com.yt.jpa.hospitalManagement.dto.response.DoctorSlotResponseDto;
import com.yt.jpa.hospitalManagement.dto.response.publicDto.DoctorPublicDto;
import com.yt.jpa.hospitalManagement.entity.User;
import com.yt.jpa.hospitalManagement.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

//    PUBLIC
    @GetMapping("")
    public ResponseEntity<List<DoctorPublicDto>> findAll() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorPublicDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok()
                .body(doctorService.getDoctorPubliclyById(id));
    }

//    Patient and Admin
    @GetMapping("/{doctorId}/slots")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN')")
    public ResponseEntity<List<DoctorSlotResponseDto>> getDoctorSlots(
            @PathVariable Long doctorId
    ) {
        return ResponseEntity.ok(
                doctorService.getSlotsByDoctor(doctorId)
        );
    }

//  Doctor by department id.
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<DoctorPublicDto>> findAllForDepartment(
            @PathVariable Long departmentId
    ) {
        return ResponseEntity.ok(doctorService.getDoctorsByDepartmentId(departmentId));
    }

    //    ADMIN ONLY
    @GetMapping("/admin/{id}")
    public ResponseEntity<DoctorResponseDto> getDoctorByAdmin(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                doctorService.getDoctorsById(id)
        );
    }

//    @PostMapping("")
//    public ResponseEntity<DoctorResponseDto> createDoctor(
//            @AuthenticationPrincipal UserDetails userDetails,
//            @Valid @RequestBody DoctorRequestDto doctorRequestDto
//    ) {
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(doctorService.createDoctor(userDetails.getUsername(), doctorRequestDto));
//    }

    @PostMapping("/admin/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponseDto> createDoctorByAdmin(
            @Valid @RequestBody AdminCreateDoctorRequestDto dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(doctorService.createDoctorByAdmin(dto));
    }


    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteDoctor(
            @PathVariable Long id
    ) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

//    DOCTOR ONLY
    @GetMapping("/me")
    public ResponseEntity<DoctorResponseDto> findById() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok()
                .body(doctorService.getDoctorsById(user.getId()));
    }

    @GetMapping("/slots")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<DoctorSlotResponseDto>> getAllDoctorSlotsByDoctor() {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(
                doctorService.getSlotsByDoctor(user.getId())
        );
    }

    @PostMapping("/slots")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> createSlot(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody DoctorSlotRequestDto dto
    ) {
        doctorService.createDoctorSlot(user.getId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("")
    public ResponseEntity<DoctorResponseDto> updateDoctor(
            @Valid @RequestBody DoctorRequestDto doctorRequestDto
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity
                .ok(doctorService.updateDoctor(user.getId(), doctorRequestDto));
    }

    @PatchMapping("")
    public ResponseEntity<DoctorResponseDto> patchUpdateDoctor(
            @Valid @RequestBody DoctorPatchRequestDto doctorPatchRequestDto
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity
                .ok(doctorService.updatePartialDoctor(user.getId(), doctorPatchRequestDto));
    }
}