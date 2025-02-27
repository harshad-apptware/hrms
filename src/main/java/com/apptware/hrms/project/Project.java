package com.apptware.hrms.project;

import com.apptware.hrms.client.Client;
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
  private LocalDate startDate;
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
