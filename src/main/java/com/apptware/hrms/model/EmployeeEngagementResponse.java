package com.apptware.hrms.model;

import lombok.Builder;

import java.util.List;
@Builder
public record EmployeeEngagementResponse(
        Long allotmentId,
        Long employeeId,
        String employeeName,
        String employeeStatus,
        List<ShadowEngagementResponse> listOfShadows
) {}
