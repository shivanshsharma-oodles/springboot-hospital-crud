package com.yt.jpa.hospitalManagement.repository;

import com.yt.jpa.hospitalManagement.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

}