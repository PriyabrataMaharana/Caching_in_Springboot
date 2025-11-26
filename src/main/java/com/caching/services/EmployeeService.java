package com.caching.services;


import com.caching.dto.EmployeeRequestDto;
import com.caching.dto.EmployeeResponseDto;
import com.caching.entity.Employee;
import java.util.Optional;

public interface EmployeeService {

    Optional<Employee> getEmployeeById(Long id);
    EmployeeResponseDto createNewEmployee(EmployeeRequestDto employeeRequestDto);
    Employee updateEmployee(Long id, Employee updatedEmp);
    void deleteEmployee(Long id);
}
