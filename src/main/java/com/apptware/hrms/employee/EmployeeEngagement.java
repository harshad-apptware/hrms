package com.apptware.hrms.employee;

import com.apptware.hrms.project.Project;
import com.fasterxml.jackson.annotation.JsonFormat;
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
  private double allocationPercent;
  @Enumerated(EnumType.STRING)
  private EngagementStatus engagementStatus;
  @ManyToOne
  @JoinColumn(name = "shadow_of")
  private EmployeeEngagement shadowOf; // Tracks which engagement this is shadowing
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
  @Column(columnDefinition = "DATE")
  private LocalDate projectJoiningDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
  @Column(columnDefinition = "DATE")
  private LocalDate projectLeavingDate;
  private String location;

  public enum EngagementStatus {
    DEPLOYED,
    SHADOW
  }
}
