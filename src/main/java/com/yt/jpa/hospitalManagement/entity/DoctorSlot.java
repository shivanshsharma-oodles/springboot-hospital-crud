package com.yt.jpa.hospitalManagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "doctor_slots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Doctor doctor;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    @OneToMany(mappedBy = "doctorSlot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;
}