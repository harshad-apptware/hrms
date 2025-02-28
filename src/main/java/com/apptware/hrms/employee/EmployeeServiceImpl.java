package com.apptware.hrms.employee;

import com.apptware.hrms.employee.Employee.EmployeeStatus;
import com.apptware.hrms.employee.EmployeeEngagement.EngagementStatus;
import com.apptware.hrms.model.EmployeeRequest;
import com.apptware.hrms.model.ProjectAllotmentRequest;
import com.apptware.hrms.project.Project;
import com.apptware.hrms.project.ProjectRepository;
import com.apptware.hrms.utils.EmailValidator;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
class EmployeeServiceImpl implements EmployeeService {

  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  EmployeeRepository employeeRepository;

  @Autowired
  EmployeeEngagementRepository engagementRepository;

  private static final int CACHE_SIZE = 100; // Maximum cache size

  private final Map<String, List<Employee>> employeeCache = Collections.synchronizedMap(new LinkedHashMap<String, List<Employee>>(CACHE_SIZE, 0.75f, true) {
    @Override
    protected boolean removeEldestEntry(Map.Entry<String, List<Employee>> eldest) {
      return size() > CACHE_SIZE;
    }
  });

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
                    .name(employeeRequest.name())
                    .contactNo(employeeRequest.contactNumber())
                    .officeEmail(officeEmail)
                    .personalEmail(employeeRequest.personalEmail())
                    .dateOfBirth(employeeRequest.dateOfBirth())
                    .dateOfJoining(LocalDate.now())
                    .designation(employeeRequest.designation())
                    .department(employeeRequest.department())
                    .totalYrExp(employeeRequest.totalYrExp())
                    .status(EmployeeStatus.NON_BILLABLE)
                    .build();

    List<EmployeeSkill> skills = new ArrayList<>();

    for (Skill skill : employeeRequest.primarySkills()) {
      skills.add(EmployeeSkill.builder()
              .employee(newEmployee)
              .skill(skill)
              .proficiency(EmployeeSkill.Proficiency.PRIMARY)
              .build());
    }

    for (Skill skill : employeeRequest.secondarySkills()) {
      skills.add(EmployeeSkill.builder()
              .employee(newEmployee)
              .skill(skill)
              .proficiency(EmployeeSkill.Proficiency.SECONDARY)
              .build());
    }
    newEmployee.setSkills(skills);
    Employee savedEmployee = employeeRepository.save(newEmployee);
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
    Optional<Employee> optionalReporting = employeeRepository.findById(allotmentRequest.reportingResource());
    Optional<Project> optionalProject = projectRepository.findById(allotmentRequest.projectId());
    if (optionalEmployee.isPresent() && optionalProject.isPresent() && optionalReporting.isPresent()) {
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
                      .reportingResource(optionalReporting.get())
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

  public List<Employee> searchEmployees(String searchTerm) {
    // Check if the result is already cached
    if (employeeCache.containsKey(searchTerm)) {
      System.out.println("Fetching employees from cache for term: " + searchTerm);
      return employeeCache.get(searchTerm);
    }

    List<Employee> employees;
    String[] names = searchTerm.split(" ");

    if (names.length == 1) {
      // Search by either first name or last name
      employees = employeeRepository.findByFirstNameContainingIgnoreCase(searchTerm);
    } else {
      // Search by both first name and last name
      employees = employeeRepository.findByFirstNameAndLastNameContainingIgnoreCase(names[0], names[1]);
    }

    // Cache the result
    if(!employees.isEmpty()) {
      employeeCache.put(searchTerm, employees);
      System.out.println("Fetching employees from database for term: " + searchTerm);

      System.out.println(employeeCache.size());
      for (Map.Entry<String, List<Employee>> m : employeeCache.entrySet()) {
        System.out.println(m.getKey() + " " + m.getValue());
      }
    }
    return employees;
  }

  @Override
  public List<EmployeeResponse> fetchEmployeesBySkills(List<Skill> skill) {
    List<Employee> employeeList = employeeRepository.findBySkills(skill);
    List<EmployeeResponse> employeeResponseList = new ArrayList<>();
    for(Employee e: employeeList){
      List<EmployeeSkill> skills = e.getSkills();
      List<Skill> primarySkills = skills.stream().filter(i -> EmployeeSkill.Proficiency.PRIMARY.equals(i.getProficiency())).map(EmployeeSkill::getSkill).toList();
      List<Skill> secondarySkills = skills.stream().filter(i -> EmployeeSkill.Proficiency.SECONDARY.equals(i.getProficiency())).map(EmployeeSkill::getSkill).toList();
      EmployeeResponse build = EmployeeResponse.builder().id(e.getId()).name(e.getName()).totalYrExp(e.getTotalYrExp()).primarySkills(primarySkills).secondarySkills(secondarySkills).status(e.getStatus()).build();
      employeeResponseList.add(build);
    }
    return employeeResponseList;
  }

  // Method to clear the cache (e.g., on employee updates)
  // Run Every 10 Days
  @Scheduled(cron = "0 0 0 */10 * *")
  public void clearCache() {
    employeeCache.clear();
  }
}
