package com.apptware.hrms.model;

import com.apptware.hrms.project.Project.BillingType;
import java.time.LocalDate;

public record ProjectRequest(
    String name, long clientId, LocalDate projectStartDate, BillingType billingType) {}
