package com.apptware.hrms.model;

import com.apptware.hrms.employee.Employee;
import lombok.Builder;

import java.util.List;

@Builder
public record EmployeeResponse(
        Long id,
        long employeeId,
        String name,
        float totalYrExp,
        List<String> primarySkills,
        List<String> secondarySkills,
        Employee.EmployeeStatus status,
        String designation,
        Employee.Department department

) {
}
