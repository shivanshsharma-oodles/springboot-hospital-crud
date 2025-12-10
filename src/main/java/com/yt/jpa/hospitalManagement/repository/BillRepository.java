package com.yt.jpa.hospitalManagement.repository;

import com.yt.jpa.hospitalManagement.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill,Integer> {
}
