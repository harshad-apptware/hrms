package com.apptware.hrms.employee;

import com.apptware.hrms.model.EmployeeRequest;
import com.apptware.hrms.model.ProjectAllotmentRequest;
import com.apptware.hrms.project.Project;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

  @Autowired EmployeeService employeeService;

  @PostMapping("/add")
  ResponseEntity<String> saveEmployee(@RequestBody EmployeeRequest employeeRequest) {
    try {
      String saved = employeeService.saveEmployee(employeeRequest);
      return ResponseEntity.ok(saved);
    } catch (IllegalArgumentException ex) {
      return ResponseEntity.ok(ex.getMessage());
    }
  }

  @GetMapping("/byId")
  ResponseEntity<Employee> getEmployee(@RequestParam long employeeId) {
    Employee employee = employeeService.findEmployeeById(employeeId);
    return ResponseEntity.ok(employee);
  }

  @GetMapping("/byProject")
  ResponseEntity<List<Employee>> getEmployeesOnProject(@RequestParam long projectId) {
    List<Employee> employees = employeeService.fetchAllEmployeesOnProject(projectId);
    return ResponseEntity.ok(employees);
  }

  @GetMapping("/byBillingStatus")
  ResponseEntity<List<Employee>> getEmployeesByBillingStatus(@RequestParam String status) {
    List<Employee> employees = employeeService.fetchAllEmployeesByBillingStatus(status);
    return ResponseEntity.ok(employees);
  }

  @GetMapping("/byEngagementStatus")
  ResponseEntity<List<Employee>> getEmployeesByEngagementStatus(
      @RequestParam String engagementStatus) {
    List<Employee> employees =
        employeeService.fetchAllEmployeesByEngagementStatus(engagementStatus);
    return ResponseEntity.ok(employees);
  }

  @GetMapping("/projectsById")
  ResponseEntity<List<Project>> getEmployeeProjects(@RequestParam long employeeId) {
    List<Project> projects = employeeService.fetchProjectsForEmployee(employeeId);
    return ResponseEntity.ok(projects);
  }

  @PostMapping("/assignProject")
  ResponseEntity<String> assignProjectToEmployee(
      @RequestBody ProjectAllotmentRequest allotmentRequest) {
    String allottedProjectToEmployee = employeeService.allotProjectToEmployee(allotmentRequest);
    return ResponseEntity.ok(allottedProjectToEmployee);
  }

  @PatchMapping("/updateEngagementStatus")
  ResponseEntity<String> updateEngagementStatus(
      @RequestParam long employeeId, @RequestParam long projectId, @RequestParam String status) {
    String engagementStatus =
        employeeService.updateEmployeeEngagementStatus(employeeId, projectId, status);
    return ResponseEntity.ok(engagementStatus);
  }

  @PatchMapping("/updateProjectRelease")
  ResponseEntity<String> updateEmployeeProjectRelease (
      @RequestParam long employeeId,
      @RequestParam long projectId,
      @RequestParam LocalDate leavingDate) {
    String status =
        employeeService.updateEmployeeProjectLeavingDate(employeeId, projectId, leavingDate);
    return ResponseEntity.ok(status);
  }
}
