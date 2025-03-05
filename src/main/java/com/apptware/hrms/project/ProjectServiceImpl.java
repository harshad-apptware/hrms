package com.apptware.hrms.project;

import com.apptware.hrms.client.Client;
import com.apptware.hrms.client.ClientRepository;
import com.apptware.hrms.model.ProjectRequest;
import com.apptware.hrms.project.Project.ProjectStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class ProjectServiceImpl implements ProjectService {

  @Autowired ProjectRepository projectRepository;

  @Autowired ClientRepository clientRepository;

  @Override
  public String saveProject(ProjectRequest projectRequest) {

    Optional<Client> optionalClient = clientRepository.findById(projectRequest.clientId());

    if (optionalClient.isPresent()) {
      Client client = optionalClient.get();
      Project newProject =
          Project.builder()
              .projectName(projectRequest.name())
              .client(client)
              .projectType(projectRequest.billingType())
              .projectStatus(ProjectStatus.ONGOING)
              .startDate(projectRequest.startDate())
                  .endDate(projectRequest.endDate())
              .build();
      projectRepository.save(newProject);
      return "Project added";
    } else {
      return "Invalid Client Id";
    }
  }

  @Override
  public Project fetchProjectById(long projectId) {
    Optional<Project> optionalProject = projectRepository.findById(projectId);
    if (optionalProject.isPresent()) {
      return optionalProject.get();
    } else {
      throw new IllegalArgumentException("Invalid Project Id.");
    }
  }

  @Override
  public List<Project> fetchProjectsByClientId(long clientId) {
    boolean clientExists = clientRepository.existsById(clientId);
    if (clientExists) {
      return projectRepository.findProjectsByClientId(clientId);
    } else {
      throw new IllegalArgumentException("Invalid Client Id.");
    }
  }

  @Override
  public String updateProjectStatus(long projectId, ProjectStatus projectStatus) {
    Optional<Project> optionalProject = projectRepository.findById(projectId);

    if (optionalProject.isPresent()) {
      Project project = optionalProject.get();
      project.setProjectStatus(projectStatus);
      projectRepository.save(project);
      return "Project Status Updated.";
    } else {
      throw new IllegalArgumentException("Invalid Project Id.");
    }
  }

  @Override
  public List<Project> fetchAllProjects() {
    return projectRepository.findAll();
  }

  @Override
  public String deleteProject(Long id) {
    Optional<Project> optionalProject = projectRepository.findById(id);
    if(optionalProject.isPresent()){
      projectRepository.deleteById(id);
      return "Project deleted";
    }
    return "Project not found";
  }

  @Override
  public String updateProject(Long id, ProjectRequest projectRequest) {
    Optional<Project> optionalProject = projectRepository.findById(id);
    Optional<Client> optionalClient = clientRepository.findById(projectRequest.clientId());
    if(optionalProject.isPresent() && optionalClient.isPresent()){
      Project project = optionalProject.get();
      project.setProjectName(projectRequest.name());
      project.setClient(optionalClient.get());
      project.setProjectType(projectRequest.billingType());
      project.setStartDate(projectRequest.startDate());
      project.setEndDate(projectRequest.endDate());
      projectRepository.save(project);
      return "Project updated";
    }
    return "Invalid ProjectId or ClientId";
  }
}
