package com.apptware.hrms.model;

import com.apptware.hrms.employee.Employee.Department;
import com.apptware.hrms.employee.EmployeeSkill;
import com.apptware.hrms.employee.Skill;

import java.time.LocalDate;
import java.util.List;

public record EmployeeRequest(
    String name,
    String officeEmail,
    String personalEmail,
    LocalDate dateOfBirth,
    String contactNumber,
    float totalYrExp,
    List<Skill> primarySkills,
    List<Skill> secondarySkills,
    String designation,
    Department department) {}
