package com.apptware.hrms.model;

import com.apptware.hrms.employee.Employee.Department;
import com.apptware.hrms.employee.Skill;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;

import java.time.LocalDate;
import java.util.List;

public record EmployeeRequest(
        String name,
        String contactNumber,
        String officeEmail,
        String personalEmail,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @Column(columnDefinition = "DATE")
        LocalDate dateOfBirth,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @Column(columnDefinition = "DATE")
        LocalDate dateOfJoining,
        Long employeeId,
        float totalYrExp,
        List<Skill> primarySkills,
        List<Skill> secondarySkills,
        String designation,
        Department department,
        Long reportingManager) {}
