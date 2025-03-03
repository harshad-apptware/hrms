package com.apptware.hrms.model;

public record ClientRequest(String clientName, String clientContact, String authorizedSignatory, String clientEmail,
                            String contactNo, String location) {
}
