package com.yt.jpa.hospitalManagement.repository;

import com.yt.jpa.hospitalManagement.entity.Doctor;
import com.yt.jpa.hospitalManagement.entity.DoctorSlot;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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

//    Pessimistic Lock of slots when fetching for appointment booking
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ds from DoctorSlot ds Where ds.id = :id")
    Optional<DoctorSlot> findByIdWithLock(@Param("id") Long id);

    int deleteByDateBefore(LocalDateTime datetime);

}

