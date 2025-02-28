package com.apptware.hrms.employee;

import com.apptware.hrms.model.EmployeeRequest;
import com.apptware.hrms.model.ProjectAllotmentRequest;
import com.apptware.hrms.project.Project;
import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {

  String saveEmployee(EmployeeRequest employeeRequest);

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
}
