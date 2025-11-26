package com.caching.mapper;

import com.caching.dto.EmployeeRequestDto;
import com.caching.dto.EmployeeResponseDto;
import com.caching.entity.Employee;

public class EmployeeMapper {

    // Convert EmployeeRequestDto --> Entity
    public static Employee toEntity(EmployeeRequestDto requestDto) {
        Employee employee = new Employee();

        employee.setEmail(requestDto.getEmail());
        employee.setName(requestDto.getName());
        employee.setSalary(requestDto.getSalary());
        employee.setDepartment(requestDto.getDepartment());

        return employee;
    }

    // Convert Entity --> EmployeeResponseDto
    public static EmployeeResponseDto toResponse(Employee employee) {
        EmployeeResponseDto responseDto = new EmployeeResponseDto();

        responseDto.setId(employee.getId());
        responseDto.setEmail(employee.getEmail());
        responseDto.setName(employee.getName());
        responseDto.setSalary(employee.getSalary());
        responseDto.setDepartment(employee.getDepartment());

        return responseDto;
    }
}
