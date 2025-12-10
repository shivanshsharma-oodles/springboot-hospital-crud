package com.yt.jpa.hospitalManagement.repository;

import com.yt.jpa.hospitalManagement.entity.Department;
import com.yt.jpa.hospitalManagement.enums.DepartmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {
    List<Department> findByStatusNot(DepartmentStatus status);
    Optional<Department> findByIdAndStatusNot(Long id, DepartmentStatus status);

    boolean existsByNameAndStatusNot(String name, DepartmentStatus status);

    boolean existsByNameAndStatusNotAndIdNot(String name, DepartmentStatus status, Long id);
}
