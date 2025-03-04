package com.apptware.hrms.employee;

import com.apptware.hrms.model.EmployeeRequest;
import com.apptware.hrms.model.EmployeeResponse;
import com.apptware.hrms.model.ProjectAllotmentRequest;
import com.apptware.hrms.project.Project;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {

  String saveEmployee(EmployeeRequest employeeRequest);

  List<EmployeeResponse> fetchAlLEmployees();

  Employee findEmployeeById(long employeeId);

  List<Employee> fetchAllEmployeesByBillingStatus(String status);

  List<Project> fetchProjectsForEmployee(long employeeId);

  List<Employee> fetchAllEmployeesOnProject(long projectId);

  List<Employee> fetchAllEmployeesByEngagementStatus(String status);

  String allotProjectToEmployee(ProjectAllotmentRequest allotmentRequest);

  String updateEmployeeEngagementStatus(long employeeId, long projectId, String status);

  String updateEmployeeProjectLeavingDate(long employeeId, long projectId, LocalDate leavingDate);

  List<Employee> searchEmployees(String searchTerm);

  List<EmployeeResponse> fetchEmployeesBySkills(List<Skill> skill);

  @Transactional
  String deleteEmployee(Long id);

  String updateEmployee(Long id, EmployeeRequest employeeRequest);
}
