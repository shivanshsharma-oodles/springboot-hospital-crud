package com.yt.jpa.hospitalManagement.entity;

import com.yt.jpa.hospitalManagement.enums.DepartmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 100,  nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private DepartmentStatus departmentStatus;
}
