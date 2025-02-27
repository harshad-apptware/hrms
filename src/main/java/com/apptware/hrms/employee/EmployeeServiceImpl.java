package com.apptware.hrms.employee;

import com.apptware.hrms.employee.Employee.EmployeeStatus;
import com.apptware.hrms.employee.EmployeeEngagement.EngagementStatus;
import com.apptware.hrms.model.EmployeeRequest;
import com.apptware.hrms.model.ProjectAllotmentRequest;
import com.apptware.hrms.project.Project;
import com.apptware.hrms.project.ProjectRepository;
import com.apptware.hrms.utils.EmailValidator;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class EmployeeServiceImpl implements EmployeeService {

  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  EmployeeRepository employeeRepository;

  @Autowired
  EmployeeEngagementRepository engagementRepository;

  @Override
  public String saveEmployee(EmployeeRequest employeeRequest) {
    String officeEmail = employeeRequest.officeEmail();

    // Normalize Email
    String normalizedEmail = EmailValidator.normalizeEmail(officeEmail);

    // Validate Email
    if (!EmailValidator.sanitizeEmail(normalizedEmail)) {
      throw new IllegalArgumentException("Invalid email. Email must belong to '@apptware.com' and contain only letters, numbers, '.' and '_'");
    }

    Optional<Employee> optionalEmployee = employeeRepository.findEmployeeByOfficeEmail(
        normalizedEmail);

    if (optionalEmployee.isPresent()) {
      throw new IllegalArgumentException("Employee already exists.");
    }


    Employee newEmployee =
        Employee.builder()
            .officeEmail(officeEmail)
            .name(employeeRequest.name())
            .contactNo(employeeRequest.contactNumber())
            .dateOfBirth(employeeRequest.dateOfBirth())
            .personalEmail(employeeRequest.personalEmail())
            .status(EmployeeStatus.NON_BILLABLE)
            .dateOfJoining(LocalDate.now())
            .build();
    employeeRepository.save(newEmployee);

    return "Employee Saved";
  }

  @Override
  public Employee findEmployeeById(long employeeId) {
    Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
    if (optionalEmployee.isPresent()) {
      return optionalEmployee.get();
    } else {
      throw new IllegalArgumentException("Invalid Employee Id.");
    }
  }

  @Override
  public List<Employee> fetchAllEmployeesByBillingStatus(String status) {
    return employeeRepository.listAllEmployeeByBillingStatus(status);
  }

  @Override
  public List<Employee> fetchAllEmployeesOnProject(long projectId) {
    boolean projectExists = projectRepository.existsById(projectId);
    if (projectExists) {
      return employeeRepository.listAllEmployeesByProjectId(projectId);
    } else {
      throw new IllegalArgumentException("Invalid Project Id.");
    }
  }

  @Override
  public List<Employee> fetchAllEmployeesByEngagementStatus(String status) {
    return employeeRepository.listAllEmployeesByEngagementStatus(status);
  }

  @Override
  public List<Project> fetchProjectsForEmployee(long employeeId) {
    boolean employeeExists = employeeRepository.existsById(employeeId);
    if (employeeExists) {
      return employeeRepository.listAllProjectsByEmployeeId(employeeId);
    } else {
      throw new IllegalArgumentException("Invalid Employee Id.");
    }
  }

  @Override
  public String allotProjectToEmployee(ProjectAllotmentRequest allotmentRequest) {
    Optional<Employee> optionalEmployee = employeeRepository.findById(allotmentRequest.employeeId());
    Optional<Project> optionalProject = projectRepository.findById(allotmentRequest.projectId());
    if (optionalEmployee.isPresent() && optionalProject.isPresent()) {
      Employee employee = optionalEmployee.get();
      Project project = optionalProject.get();
      EmployeeEngagement employeeEngagement =
          EmployeeEngagement.builder()
              .employee(employee)
              .project(project)
              .projectJoiningDate(allotmentRequest.projectJoiningDate())
              .engagementStatus(allotmentRequest.engagementStatus())
              .allocationPercent(allotmentRequest.allocationPercent())
              .location(allotmentRequest.workLocation())
              .build();
      engagementRepository.save(employeeEngagement);

      if (EmployeeStatus.NON_BILLABLE.equals(employee.getStatus())
          && EngagementStatus.DEPLOYED.equals(allotmentRequest.engagementStatus())) {
        employee.setStatus(EmployeeStatus.BILLABLE);
        employeeRepository.save(employee);
      }

      return "Project Allocated.";
    } else {
      throw new IllegalArgumentException("Invalid ProjectId or EmployeeId.");
    }
  }

  @Override
  public String updateEmployeeEngagementStatus(long employeeId, long projectId, String status) {
    boolean employeeExists = employeeRepository.existsById(employeeId);
    boolean projectExists = projectRepository.existsById(projectId);

    if (employeeExists && projectExists) {
      engagementRepository.setEmployeeEngagementStatus(employeeId, projectId, status);
      return "Project Allocated to Employee.";
    } else {
      throw new IllegalArgumentException("Invalid ProjectId or EmployeeId.");
    }
  }

  @Override
  public String updateEmployeeProjectLeavingDate(long employeeId, long projectId,
      LocalDate leavingDate) {
    boolean employeeExists = employeeRepository.existsById(employeeId);
    boolean projectExists = projectRepository.existsById(projectId);

    if (employeeExists && projectExists) {
      engagementRepository.setEmployeeProjectLeavingDate(employeeId, projectId, leavingDate);
      return "Project Leaving Date updated to Employee.";
    } else {
      throw new IllegalArgumentException("Invalid ProjectId or EmployeeId.");
    }
  }
}
