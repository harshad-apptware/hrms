package com.apptware.hrms.model;

import com.apptware.hrms.employee.EmployeeEngagement.EngagementStatus;
import java.time.LocalDate;

public record ProjectAllotmentRequest(
    long projectId,
    long employeeId,
    String workLocation,
    double allocationPercent,
    LocalDate projectJoiningDate,
    EngagementStatus engagementStatus) {}
