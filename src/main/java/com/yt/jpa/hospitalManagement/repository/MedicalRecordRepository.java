package com.yt.jpa.hospitalManagement.repository;

import com.yt.jpa.hospitalManagement.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord,Integer> {

}
