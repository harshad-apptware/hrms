package com.apptware.hrms.employee;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

  @Modifying
  @Query("UPDATE EmployeeEngagement e SET e.employee = NULL WHERE e.employee.id = :employeeId")
  void updateEmployeeEngagement(@Param("employeeId") Long employeeId);

  List<EmployeeEngagement> findByEmployeeId(long employeeId);

  @Query("SELECT ee FROM EmployeeEngagement ee WHERE ee.shadowOf.id = :shadowOfId")
  List<EmployeeEngagement> findByShadowOfId(@Param("shadowOfId") Long shadowOfId);

}
