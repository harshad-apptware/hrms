package com.apptware.hrms.employee;

import com.apptware.hrms.model.EmployeeRequest;
import com.apptware.hrms.model.ProjectAllotmentRequest;
import com.apptware.hrms.project.Project;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
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

  @GetMapping("/search")
  public ResponseEntity<?> searchEmployees( @RequestBody EmployeeNameSearchRecord employeeNameSearchRecord) {
    String searchTerm = employeeNameSearchRecord.name();
    if (!searchTerm.isEmpty()) {
      List<Employee> employees = employeeService.searchEmployees(searchTerm.trim());
      if (!employees.isEmpty()) {
        return ResponseEntity.ok(employees);
      } else {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "No such employee found");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
      }
    }
    else {
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("message", "No such employee found");
      return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
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

  @GetMapping("/bySkills")
  ResponseEntity<?> getEmployeesBySkills(
          @RequestBody List<Skill> skill) {
    List<EmployeeResponse> employees =
            employeeService.fetchEmployeesBySkills(skill);

    if(!employees.isEmpty()){
      return ResponseEntity.ok(employees);
    }
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Employee found with skill");
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
