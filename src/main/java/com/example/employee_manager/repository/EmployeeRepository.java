package com.example.employee_manager.repository;

import com.example.employee_manager.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // SELECT COUNT(*) > 0 FROM employees WHERE email = ?
    boolean existsByEmail(String email);

    // Same check, but ignores the employee being edited (used when updating)
    boolean existsByEmailAndIdNot(String email, Long id);

    // Task 11 (challenge): WHERE LOWER(full_name) LIKE LOWER('%keyword%')
    List<Employee> findByFullNameContainingIgnoreCase(String keyword);
}