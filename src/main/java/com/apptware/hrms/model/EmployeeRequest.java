package com.apptware.hrms.model;

import com.apptware.hrms.employee.Employee.Department;
import java.time.LocalDate;

public record EmployeeRequest(
    String name,
    String officeEmail,
    String personalEmail,
    LocalDate dateOfBirth,
    String contactNumber,
    String designation,
    Department department) {}
