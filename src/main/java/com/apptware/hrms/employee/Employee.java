package com.apptware.hrms.employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String contactNo;
  private String officeEmail;
  private String personalEmail;

  @Column(columnDefinition = "DATE")
  private LocalDate dateOfBirth;

  @Column(columnDefinition = "DATE")
  private LocalDate dateOfJoining;

  @Column(columnDefinition = "DATE")
  private LocalDate dateOfLeaving;

  private String designation;

  @Enumerated(EnumType.STRING)
  private Department department;

  @Enumerated(EnumType.STRING)
  private EmployeeStatus status;

  //  @ManyToOne
  //  @JoinColumn(name = "reporting_manager_id")
  //  private Employee reportingManager;

  public enum EmployeeStatus {
    BILLABLE,
    NON_BILLABLE
  }

  public enum Department {
    HR,
    SALES,
    DESIGN,
    ACCOUNTS,
    MARKETING,
    OPERATIONS,
    TECHNOLOGY
  }
}
