package com.apptware.hrms.employee;

import lombok.Builder;

import java.util.List;

@Builder
public record EmployeeResponse(
        long id,
        String name,
        float totalYrExp,
        List<Skill> primarySkills,
        List<Skill> secondarySkills,
        Employee.EmployeeStatus status
) {
}
