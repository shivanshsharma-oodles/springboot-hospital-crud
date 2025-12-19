package com.yt.jpa.hospitalManagement.entity;

import com.yt.jpa.hospitalManagement.embeddable.Address;
import com.yt.jpa.hospitalManagement.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

//    @Column(length = 100, nullable = false)
//    private String email;

    //    Linking to auth user (email, password, roles)
    @OneToOne
    @MapsId // it tells if patient does not exist without user, then there is no need of two ids in Patient so, hibernate will set id == user_id
    private User user;

    @Column(length = 15, nullable = false)
    private String phone;

    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Embedded
    public Address address;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
