package com.apptware.hrms.employee;

import com.apptware.hrms.employee.Employee.EmployeeStatus;
import com.apptware.hrms.employee.EmployeeEngagement.EngagementStatus;
import com.apptware.hrms.model.*;
import com.apptware.hrms.project.Project;
import com.apptware.hrms.project.ProjectRepository;
import com.apptware.hrms.utils.EmailValidator;
import java.time.LocalDate;
import java.util.*;

import jakarta.transaction.Transactional;
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
                    .dateOfJoining(employeeRequest.dateOfJoining())
                    .designation(employeeRequest.designation())
                    .department(employeeRequest.department())
                    .totalYrExp(employeeRequest.totalYrExp())
                    .status(EmployeeStatus.NON_BILLABLE)
                    .employeeId(employeeRequest.employeeId())
                    .dateOfJoining(employeeRequest.dateOfJoining())
                    .build();

    if (employeeRequest.reportingManager() != null) {
      Optional<Employee> reportingManager = employeeRepository.findById(employeeRequest.reportingManager());
      if (reportingManager.isEmpty()) {
        throw new IllegalArgumentException("Invalid Reporting Manager Id");
      }
      newEmployee.setReportingManager(reportingManager.get());
    }


    List<EmployeeSkill> skills = new ArrayList<>();

    for (String skillDescription : employeeRequest.primarySkills()) {
      Skill skill = Skill.fromDescription(skillDescription);
      skills.add(EmployeeSkill.builder()
              .employee(newEmployee)
              .skill(skill)
              .proficiency(EmployeeSkill.Proficiency.PRIMARY)
              .build());
    }

    for (String skillDescription : employeeRequest.secondarySkills()) {
      Skill skill = Skill.fromDescription(skillDescription);
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
  public List<EmployeeResponse> fetchAlLEmployees() {
    List<Employee> employeeList = employeeRepository.findAll();
    List<EmployeeResponse> employeeResponseList = new ArrayList<>();
    for (Employee e : employeeList) {
      List<EmployeeSkill> skills = e.getSkills();

      List<String> primarySkills = skills.stream()
              .filter(i -> EmployeeSkill.Proficiency.PRIMARY.equals(i.getProficiency()))
              .map(i -> i.getSkill().getDescription())
              .toList();

      List<String> secondarySkills = skills.stream()
              .filter(i -> EmployeeSkill.Proficiency.SECONDARY.equals(i.getProficiency()))
              .map(i -> i.getSkill().getDescription())
              .toList();

      EmployeeResponse build = EmployeeResponse.builder()
              .id(e.getId())
              .employeeId(e.getEmployeeId())
              .name(e.getName())
              .totalYrExp((float) e.getTotalYrExp()) // Assuming totalYrExp is Double in Employee
              .primarySkills(primarySkills)
              .secondarySkills(secondarySkills)
              .status(e.getStatus())
              .designation(e.getDesignation())
              .department(e.getDepartment())
              .build();

      employeeResponseList.add(build);
    }
    return employeeResponseList;
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
    try {
      EmployeeStatus enumStatus = EmployeeStatus.valueOf(status);
      return employeeRepository.findByStatus(enumStatus);
      // Proceed with the query
    } catch (IllegalArgumentException e) {
      // Handle the case where statusStr does not match any enum value
      throw new IllegalArgumentException("Invalid Status: "+ status);
    }
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
    try {
      EngagementStatus enumStatus = EngagementStatus.valueOf(status);
      return employeeRepository.listAllEmployeesByEngagementStatus(enumStatus);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid engagement status: " + status);
    }
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
    Optional<Employee> optionalEmployee = employeeRepository.findById(allotmentRequest.id());
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

      if (Objects.nonNull(allotmentRequest.shadowOf())) {
        Optional<EmployeeEngagement> optionalShadow = engagementRepository.findById(allotmentRequest.shadowOf());
        if (optionalShadow.isPresent()) {
          EmployeeEngagement reportingResource = optionalShadow.get();
          employeeEngagement.setShadowOf(reportingResource);
        } else {
          return "Invalid Reporting Resource.";
        }
      }

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
    }
    return employees;
  }

  @Override
  public List<EmployeeResponse> fetchEmployeesBySkills(List<Skill> skills) {
    long skillSize = skills.size();  // Number of skills in input
    List<Employee> employeeList = employeeRepository.findBySkills(skills, skillSize);
    List<EmployeeResponse> employeeResponseList = new ArrayList<>();

    for (Employee e : employeeList) {
      List<EmployeeSkill> skillList = e.getSkills();

      List<String> primarySkills = skillList.stream()
              .filter(i -> EmployeeSkill.Proficiency.PRIMARY.equals(i.getProficiency()))
              .map(i -> i.getSkill().getDescription())
              .toList();

      List<String> secondarySkills = skillList.stream()
              .filter(i -> EmployeeSkill.Proficiency.SECONDARY.equals(i.getProficiency()))
              .map(i -> i.getSkill().getDescription())
              .toList();

      EmployeeResponse build = EmployeeResponse.builder()
              .id(e.getId())
              .employeeId(e.getEmployeeId())
              .name(e.getName())
              .totalYrExp((float) e.getTotalYrExp()) // Assuming totalYrExp is Double in Employee
              .primarySkills(primarySkills)
              .secondarySkills(secondarySkills)
              .status(e.getStatus())
              .build();

      employeeResponseList.add(build);
    }

    return employeeResponseList;
  }

  @Transactional
  @Override
  public String deleteEmployee(Long id) {
    Optional<Employee> optionalEmployee = employeeRepository.findById(id);
    if(optionalEmployee.isPresent()){
      engagementRepository.updateEmployeeEngagement(id);
      employeeRepository.deleteById(id);
      return "Employee deleted";
    }
    return "Employee not found";
  }

  @Override
  public String updateEmployee(Long id, EmployeeRequest employeeRequest) {
    Optional<Employee> optionalEmployee = employeeRepository.findById(id);
    Optional<Employee> optionalReportingManager = employeeRepository.findById(employeeRequest.reportingManager());

    if (optionalEmployee.isPresent() && optionalReportingManager.isPresent()) {
      Employee employee = optionalEmployee.get();
      employee.setName(employeeRequest.name());
      employee.setContactNo(employeeRequest.contactNumber());
      employee.setOfficeEmail(employeeRequest.officeEmail());
      employee.setPersonalEmail(employeeRequest.personalEmail());
      employee.setDateOfBirth(employeeRequest.dateOfBirth());
      employee.setDateOfJoining(employeeRequest.dateOfJoining());
      employee.setEmployeeId(employeeRequest.employeeId());
      employee.setTotalYrExp(employeeRequest.totalYrExp());
      employee.setDesignation(employeeRequest.designation());
      employee.setDepartment(employeeRequest.department());
      employee.setReportingManager(optionalReportingManager.get());

      // Update existing skills
      if (employee.getSkills() != null && !employee.getSkills().isEmpty()) {
        Map<String, EmployeeSkill> existingSkills = new HashMap<>();
        for (EmployeeSkill skill : employee.getSkills()) {
          existingSkills.put(skill.getSkill().getDescription(), skill);
        }

        List<EmployeeSkill> skillsToRemove = new ArrayList<>();
        List<EmployeeSkill> skillsToAdd = new ArrayList<>();

        for (String skillDescription : employeeRequest.primarySkills()) {
          Skill skill = Skill.fromDescription(skillDescription);
          if (existingSkills.containsKey(skillDescription)) {
            existingSkills.get(skillDescription).setProficiency(EmployeeSkill.Proficiency.PRIMARY);
          } else {
            skillsToAdd.add(EmployeeSkill.builder()
                    .employee(employee)
                    .skill(skill)
                    .proficiency(EmployeeSkill.Proficiency.PRIMARY)
                    .build());
          }
        }

        for (String skillDescription : employeeRequest.secondarySkills()) {
          Skill skill = Skill.fromDescription(skillDescription);
          if (existingSkills.containsKey(skillDescription)) {
            existingSkills.get(skillDescription).setProficiency(EmployeeSkill.Proficiency.SECONDARY);
          } else {
            skillsToAdd.add(EmployeeSkill.builder()
                    .employee(employee)
                    .skill(skill)
                    .proficiency(EmployeeSkill.Proficiency.SECONDARY)
                    .build());
          }
        }

        // Remove skills that are not in the new list
        for (EmployeeSkill skill : existingSkills.values()) {
          if (!employeeRequest.primarySkills().contains(skill.getSkill().getDescription()) &&
                  !employeeRequest.secondarySkills().contains(skill.getSkill().getDescription())) {
            skillsToRemove.add(skill);
          }
        }

        // Remove skills from the employee
        employee.getSkills().removeAll(skillsToRemove);

        // Add new skills to the employee
        employee.getSkills().addAll(skillsToAdd);
      } else {
        List<EmployeeSkill> skills = new ArrayList<>();

        for (String skillDescription : employeeRequest.primarySkills()) {
          Skill skill = Skill.fromDescription(skillDescription);
          skills.add(EmployeeSkill.builder()
                  .employee(employee)
                  .skill(skill)
                  .proficiency(EmployeeSkill.Proficiency.PRIMARY)
                  .build());
        }

        for (String skillDescription : employeeRequest.secondarySkills()) {
          Skill skill = Skill.fromDescription(skillDescription);
          skills.add(EmployeeSkill.builder()
                  .employee(employee)
                  .skill(skill)
                  .proficiency(EmployeeSkill.Proficiency.SECONDARY)
                  .build());
        }
        employee.setSkills(skills);
      }
      employeeRepository.save(employee);
      return "Employee Updated";
    }
    return "Employee does not exists";
  }

  public EmployeeEngagementResponse fetchEmployeeEngagement(long employeeId) {
    // Fetch all engagements for the employee
    List<EmployeeEngagement> engagements = engagementRepository.findByEmployeeId(employeeId);

    if (engagements.isEmpty()) {
      return null; // Or throw an exception if no engagements are found
    }

    // Assuming we're interested in the first engagement for simplicity
    EmployeeEngagement engagement = engagements.get(0);

    // Fetch shadows for this engagement
    List<EmployeeEngagement> shadowsEngagements = engagementRepository.findByShadowOfId(employeeId);

    List<ShadowEngagementResponse> listOfShadows = new ArrayList<>();
    for (EmployeeEngagement shadowEngagement : shadowsEngagements) {
      Employee shadowEmployee = employeeRepository.findById(shadowEngagement.getEmployee().getId()).orElse(null);
      Project shadowProject = projectRepository.findById(shadowEngagement.getProject().getId()).orElse(null);

      if (shadowEmployee != null && shadowProject != null) {
        ShadowEngagementResponse shadowResponse = new ShadowEngagementResponse(
                shadowEmployee.getId(),
                shadowEmployee.getName(),
                shadowProject.getProjectName(),
                String.format("%.0f%%", shadowEngagement.getAllocationPercent())
        );
        listOfShadows.add(shadowResponse);
      }
    }

    Employee employee = employeeRepository.findById(engagement.getEmployee().getId()).orElse(null);

    if (employee != null) {
      EmployeeEngagementResponse response = new EmployeeEngagementResponse(
              engagement.getId(),
              employee.getId(),
              employee.getName(),
              engagement.getEngagementStatus().toString(),
              listOfShadows
      );
      return response;
    } else {
      return null; // Or throw an exception if employee is not found
    }
  }

  // Method to clear the cache (e.g., on employee updates)
  // Run Every 10 Days
  @Scheduled(cron = "0 0 0 */10 * *")
  public void clearCache() {
    employeeCache.clear();
  }
}
