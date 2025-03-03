package com.apptware.hrms.employee;

import com.apptware.hrms.project.Project;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
interface EmployeeRepository extends JpaRepository<Employee, Long> {

  @Query("SELECT e FROM Employee e WHERE e.officeEmail = :officeEmail")
  Optional<Employee> findEmployeeByOfficeEmail(@Param("officeEmail") String officeEmail);

  @Query(
      "SELECT e FROM Employee e WHERE LOWER(SUBSTRING(e.name, 1, LOCATE(' ', e.name) - 1)) = LOWER(:name)")
  List<Employee> findByFirstNameContainingIgnoreCase(@Param("name") String name);

  @Query(
      "SELECT e FROM Employee e WHERE LOWER(SUBSTRING(e.name, 1, LOCATE(' ', e.name) - 1)) = LOWER(:firstName) AND LOWER(SUBSTRING(name, LOCATE(' ', e.name) + 1, LENGTH(e.name))) = LOWER(:lastName)")
  List<Employee> findByFirstNameAndLastNameContainingIgnoreCase(
      @Param("firstName") String firstName, @Param("lastName") String lastName);

//  @Query("SELECT e FROM Employee e WHERE e.status = :status")
  List<Employee> findByStatus(Employee.EmployeeStatus status);

  @Query(
      "SELECT e FROM Employee e WHERE e.id IN (SELECT ee.employee.id FROM EmployeeEngagement ee WHERE ee.project.id = :projectId)")
  List<Employee> listAllEmployeesByProjectId(@Param("projectId") long projectId);

  @Query("SELECT e FROM Employee e WHERE e.id IN (SELECT ee.employee.id FROM EmployeeEngagement ee where ee.engagementStatus = :status)")
  List<Employee> listAllEmployeesByEngagementStatus(@Param("status") EmployeeEngagement.EngagementStatus status);

  @Query("SELECT p FROM Project p WHERE p.id IN (SELECT ee.project.id FROM EmployeeEngagement ee WHERE ee.employee.id = :employeeId)")
  List<Project> listAllProjectsByEmployeeId(@Param("employeeId") long employeeId);

  @Query("SELECT e FROM Employee e " +
          "JOIN e.skills es " +
          "WHERE es.skill IN :skills " +
          "GROUP BY e " +
          "HAVING COUNT(DISTINCT es.skill) = :skillSize")
  List<Employee> findBySkills(@Param("skills") List<Skill> skills, @Param("skillSize") long skillSize);


}
