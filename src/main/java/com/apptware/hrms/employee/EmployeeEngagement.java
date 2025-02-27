package com.apptware.hrms.employee;

import com.apptware.hrms.project.Project;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "employee_engagements")
public class EmployeeEngagement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @ManyToOne
  @JoinColumn(name = "employee_id")
  private Employee employee;
  @ManyToOne
  @JoinColumn(name = "project_id")
  private Project project;
  @Enumerated(EnumType.STRING)
  private EngagementStatus engagementStatus;
  private double allocationPercent;
  private LocalDate projectJoiningDate;
  private LocalDate projectLeavingDate;
  private String location;

  public enum EngagementStatus {
    DEPLOYED,
    SHADOW
  }
}
