package com.yt.jpa.hospitalManagement.repository;

import com.yt.jpa.hospitalManagement.entity.Doctor;
import com.yt.jpa.hospitalManagement.enums.DoctorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

//    Doctor Exists by Email or Phone but status is not != (specify)
    boolean existsByUser_EmailAndStatusNot(String email, DoctorStatus status);
    boolean existsByPhoneAndStatusNot(String phone, DoctorStatus status);

//    Doctor Exists by Email or Phone but status is not != (specify) & is nto the doctor with same id.
    boolean existsByUser_EmailAndStatusNotAndIdNot(String email, DoctorStatus status, Long id);
    boolean existsByPhoneAndStatusNotAndIdNot(String phone,DoctorStatus status, Long id);

//    All Doctors with status != archived
    List<Doctor> findByStatusNot(DoctorStatus status);
    Optional<Doctor> findByIdAndStatusNot(Long id, DoctorStatus status);
}