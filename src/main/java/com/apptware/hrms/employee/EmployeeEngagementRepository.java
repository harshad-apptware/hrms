package com.apptware.hrms.employee;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeEngagementRepository extends JpaRepository<EmployeeEngagement, Long> {

  @Query(
      "UPDATE EmployeeEngagement ee SET ee.engagementStatus = :status WHERE ee.employee.id = :employeeId AND ee.project.id = :projectId")
  void setEmployeeEngagementStatus(
      @Param("employeeId") long employeeId,
      @Param("projectId") long projectId,
      @Param("status") String status);

  @Query(
      "UPDATE EmployeeEngagement ee SET ee.engagementStatus = :status WHERE ee.employee.id = :employeeId AND ee.projectLeavingDate = :leavingDate")
  void setEmployeeProjectLeavingDate(
      @Param("employeeId") long employeeId,
      @Param("projectId") long projectId,
      @Param("leavingDate")LocalDate leavingDate);
}
