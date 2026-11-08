package com.example.employee_manager.service;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    // Task 11: search by name (returns everything when the keyword is empty)
    public List<Employee> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return employeeRepository.findAll();
        }
        return employeeRepository.findByFullNameContainingIgnoreCase(keyword.trim());
    }

    public Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + id));
    }

    // id == null  -> checking a NEW employee
    // id != null  -> checking while EDITING that employee (ignore its own email)
    public boolean emailExists(String email, Long id) {
        return (id == null)
                ? employeeRepository.existsByEmail(email)
                : employeeRepository.existsByEmailAndIdNot(email, id);
    }

    public Employee save(Employee employee) {
        employee.setId(null); // always INSERT a new row
        return employeeRepository.save(employee);
    }

    public Employee update(Long id, Employee changes) {
        Employee existing = findById(id);
        existing.setFullName(changes.getFullName());
        existing.setEmail(changes.getEmail());
        existing.setPhone(changes.getPhone());
        existing.setDepartment(changes.getDepartment());
        existing.setSalary(changes.getSalary());
        return employeeRepository.save(existing);
    }

    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    public long count() {
        return employeeRepository.count();
    }
}