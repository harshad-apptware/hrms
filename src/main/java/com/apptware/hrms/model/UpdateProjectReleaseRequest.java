package com.apptware.hrms.model;

import java.time.LocalDate;

public record UpdateProjectReleaseRequest(long employeeId, long projectId, LocalDate leavingDate) {
}
