package com.caching.dto;

import com.caching.entity.Department;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeResponseDto {
    private Long id;
    private String name;
    private String email;
    private Long salary;
    private Department department;
}
