package com.yt.jpa.hospitalManagement.repository;

import com.yt.jpa.hospitalManagement.entity.Department;
import com.yt.jpa.hospitalManagement.enums.DepartmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {
    List<Department> findByDepartmentStatusNot(DepartmentStatus status);
    Optional<Department> findByIdAndDepartmentStatusNot(Long id, DepartmentStatus status);

    boolean existsByNameAndDepartmentStatusNot(String name, DepartmentStatus status);

    boolean existsByNameAndDepartmentStatusNotAndIdNot(String name, DepartmentStatus status, Long id);
}