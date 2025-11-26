package com.caching.dto;

import com.caching.entity.Department;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequestDto {
    private String name;
    private String email;
    private Long salary;
    private Department department;
}
