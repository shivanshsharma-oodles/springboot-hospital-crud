package com.yt.jpa.hospitalManagement.entity;

import com.yt.jpa.hospitalManagement.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus;

//    Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    private Doctor doctor; // 1 Doctor → Many Appointments

    @ManyToOne(fetch = FetchType.LAZY)
    private Patient patient; // 1 Patient → Many Appointments

    @OneToOne(mappedBy = "appointment" , cascade = CascadeType.ALL)
    private Bill bill;

    @ManyToOne
    private DoctorSlot doctorSlot;

    @OneToOne(mappedBy = "appointment")
    private MedicalRecord medicalRecord;
}

