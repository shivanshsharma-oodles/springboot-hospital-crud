package com.yt.jpa.hospitalManagement.controller;

import com.yt.jpa.hospitalManagement.dto.request.DepartmentPatchRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.DepartmentRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.DepartmentResponseDto;
import com.yt.jpa.hospitalManagement.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping("")
    public ResponseEntity<List<DepartmentResponseDto>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    @PostMapping("")
    public ResponseEntity<DepartmentResponseDto> createDepartment(@Valid @RequestBody DepartmentRequestDto departmentRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(departmentService.createDepartment(departmentRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentRequestDto departmentRequestDto
    ) {
        return ResponseEntity
                .ok(departmentService.updateDepartment(id, departmentRequestDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> patchUpdateDepartment(
            @PathVariable  Long id,
            @Valid @RequestBody DepartmentPatchRequestDto departmentPatchRequestDto
            ){
        return ResponseEntity
                .ok(departmentService.updatePartialDepartment(id, departmentPatchRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment( @PathVariable Long id){
        departmentService.deleteDepartment(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
