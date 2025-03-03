package com.apptware.hrms.model;

import com.apptware.hrms.employee.Employee;
import com.apptware.hrms.employee.Skill;
import lombok.Builder;

import java.util.List;

@Builder
public record EmployeeResponse(
        long id,
        String name,
        float totalYrExp,
        List<String> primarySkills,
        List<String> secondarySkills,
        Employee.EmployeeStatus status
) {
}
