package com.yt.jpa.hospitalManagement.repository;

import com.yt.jpa.hospitalManagement.entity.Doctor;
import com.yt.jpa.hospitalManagement.entity.DoctorSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface DoctorSlotRepository extends JpaRepository<DoctorSlot, Long> {

    /* Kya is doctor ke liye, is date par, koi aisa slot already exist karta hai jo naye slot ke time ke saath overlap karta ho? */
    boolean existsByDoctorAndDateAndStartTimeLessThanAndEndTimeGreaterThan(
            Doctor doctor,
            LocalDate date,
            LocalTime endTime,
            LocalTime startTime
    );

    List<DoctorSlot> findByDoctorAndDateGreaterThanEqual(
            Doctor doctor,
            LocalDate date
    );

    List<DoctorSlot> findByDoctorAndDateGreaterThanEqualAndIdNotIn(
            Doctor doctor,
            LocalDate date,
            List<Long> ids
    );


}

