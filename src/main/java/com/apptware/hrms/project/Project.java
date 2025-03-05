package com.apptware.hrms.project;

import com.apptware.hrms.client.Client;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

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
@Table(name = "project")
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String projectName;
  @ManyToOne
  @JoinColumn(name = "client_id")
  private Client client;
  @Enumerated(EnumType.STRING)
  private ProjectStatus projectStatus;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
  @Column(columnDefinition = "DATE")
  private LocalDate startDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
  @Column(columnDefinition = "DATE")
  private LocalDate endDate;
  @Enumerated(EnumType.STRING)
  private BillingType projectType;

  public enum ProjectStatus {
    ONGOING,
    COMPLETED
  }

  public enum BillingType {
    BILLABLE,
    NON_BILLABLE
  }
}
