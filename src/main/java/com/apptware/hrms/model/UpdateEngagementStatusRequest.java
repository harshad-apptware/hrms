package com.apptware.hrms.model;

public record UpdateEngagementStatusRequest(long employeeId, long projectId, String status) {
}
