package com.apptware.hrms.model;

public record ShadowEngagementResponse(
        Long shadowId,
        String employeeName,
        String shadowProject,
        String employeeAllocationPercent
) {}