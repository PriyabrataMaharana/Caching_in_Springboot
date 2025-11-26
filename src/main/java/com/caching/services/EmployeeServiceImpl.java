package com.caching.services;

import com.caching.dto.EmployeeRequestDto;
import com.caching.dto.EmployeeResponseDto;
import com.caching.entity.Department;
import com.caching.entity.Employee;
import com.caching.exceptions.ResourceNotFoundException;
import com.caching.mapper.EmployeeMapper;
import com.caching.repository.DepartmentRepository;
import com.caching.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final String CACHE_NAME = "employees";

    @Override
    @Cacheable(cacheNames = CACHE_NAME, key = "#id")
    public Optional<Employee> getEmployeeById(Long id) {
        log.info("Fetching employee with id: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Employee not found with id: {}", id);
                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });
        return Optional.of(employee);
    }

    @Override
    @CachePut(cacheNames = CACHE_NAME, key = "#result.id")
    public EmployeeResponseDto createNewEmployee(EmployeeRequestDto employeeRequestDto) {
        log.info("Creating new employee with email: {}", employeeRequestDto.getEmail());

        // 🔹 Check duplicate employee
        List<Employee> existingEmployees = employeeRepository.findByEmail(employeeRequestDto.getEmail());
        if (!existingEmployees.isEmpty()) {
            throw new ResourceNotFoundException("Employee already exists with email: " + employeeRequestDto.getEmail());
        }

        // 🔹 Map Request → Entity
        Employee employee = EmployeeMapper.toEntity(employeeRequestDto);

        // 🔹 Check for existing department by name
        if (employee.getDepartment() != null && employee.getDepartment().getName() != null) {
            Department department = departmentRepository
                    .findByNameIgnoreCase(employee.getDepartment().getName())
                    .orElseGet(() -> departmentRepository.save(employee.getDepartment())); // reuse or save

            employee.setDepartment(department);
        }

        // 🔹 Save employee
        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Successfully created employee with id: {}", savedEmployee.getId());

        return EmployeeMapper.toResponse(savedEmployee);
    }

    @Override
    @CachePut(cacheNames = CACHE_NAME, key = "#id")
    public Employee updateEmployee(Long id, Employee updatedEmp) {
        log.info("Updating employee with id: {}", id);
        Employee existingEmp = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        existingEmp.setEmail(updatedEmp.getEmail());
        existingEmp.setName(updatedEmp.getName());
        existingEmp.setSalary(updatedEmp.getSalary());

        if (updatedEmp.getDepartment() != null && updatedEmp.getDepartment().getName() != null) {
            Department department = departmentRepository
                    .findByNameIgnoreCase(updatedEmp.getDepartment().getName())
                    .orElseGet(() -> departmentRepository.save(updatedEmp.getDepartment()));
            existingEmp.setDepartment(department);
        }

        Employee saved = employeeRepository.save(existingEmp);
        log.info("Successfully updated employee with id: {}", id);
        return saved;
    }

    @Override
    public void deleteEmployee(Long id) {
        log.info("Deleting employee with id: {}", id);
        Employee existingEmp = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        employeeRepository.delete(existingEmp);
        log.info("Employee deleted successfully with id: {}", id);
    }
}

