package com.caching.controller;


import com.caching.dto.EmployeeRequestDto;
import com.caching.dto.EmployeeResponseDto;
import com.caching.entity.Employee;
import com.caching.exceptions.ResourceNotFoundException;
import com.caching.mapper.EmployeeMapper;
import com.caching.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // ✅ GET Employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable Long id) {
        Optional<Employee> employeeOpt = employeeService.getEmployeeById(id);
        Employee employee = employeeOpt.orElseThrow(() ->
                new ResourceNotFoundException("Employee not found with id: " + id));
        return ResponseEntity.ok(EmployeeMapper.toResponse(employee));
    }

    // ✅ CREATE Employee
    @PostMapping("/add")
    public ResponseEntity<EmployeeResponseDto> addEmployee(@RequestBody EmployeeRequestDto employeeRequestDto) {
        EmployeeResponseDto savedEmployee = employeeService.createNewEmployee(employeeRequestDto);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    // ✅ UPDATE Employee
    @PutMapping("/update/{id}")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeRequestDto requestDto) {

        Employee updatedEmployeeEntity = employeeService.updateEmployee(id, EmployeeMapper.toEntity(requestDto));
        EmployeeResponseDto responseDto = EmployeeMapper.toResponse(updatedEmployeeEntity);
        return ResponseEntity.ok(responseDto);
    }

    // ✅ DELETE Employee
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("Employee deleted successfully with id: " + id);
    }
}
