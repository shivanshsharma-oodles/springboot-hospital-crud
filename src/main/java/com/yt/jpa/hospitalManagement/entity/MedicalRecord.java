package com.yt.jpa.hospitalManagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    private Patient patient;

    @OneToOne(fetch = FetchType.LAZY)
    private Appointment appointment;

    private String symptoms;
    private String diagnosis;

    private LocalDate followUpDate;

    private Double temperature;
    private Integer pulse;
    private Integer bpSystolic;
    private Integer bpDiastolic;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
