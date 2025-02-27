package com.apptware.hrms.model;

import java.time.LocalDate;

public record EmployeeRequest(
    String name,
    String officeEmail,
    String personalEmail,
    LocalDate dateOfBirth,
    String contactNumber) {}
