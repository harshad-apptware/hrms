package com.apptware.hrms.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

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
  private Long employeeId;
  private String contactNo;
  private String officeEmail;
  private String personalEmail;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
  @Column(columnDefinition = "DATE")
  private LocalDate dateOfBirth;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
  @Column(columnDefinition = "DATE")
  private LocalDate dateOfJoining;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
  @Column(columnDefinition = "DATE")
  private LocalDate dateOfLeaving;

  private float totalYrExp;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<EmployeeSkill> skills;

  private String designation;

  @Enumerated(EnumType.STRING)
  private Department department;

  @Enumerated(EnumType.STRING)
  private EmployeeStatus status;

  @ManyToOne
  @JoinColumn(name = "reporting_manager_id")
  private Employee reportingManager;

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
