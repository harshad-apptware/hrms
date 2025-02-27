package com.apptware.hrms.project;

import com.apptware.hrms.client.Client;
import com.apptware.hrms.client.ClientRepository;
import com.apptware.hrms.model.AddProjectRequest;
import com.apptware.hrms.project.Project.ProjectStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class ProjectServiceImpl implements ProjectService{

  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  ClientRepository clientRepository;

  @Override
  public String saveProject(AddProjectRequest projectRequest) {

    Optional<Client> optionalClient = clientRepository.findById(projectRequest.clientId());

    if (optionalClient.isPresent()){
      Client client = optionalClient.get();
      Project newProject =
          Project.builder().projectName(projectRequest.name()).client(client).build();
      projectRepository.save(newProject);
      return "Project Added";
    } else {
      throw new IllegalArgumentException("Invalid Client Id.");
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
}
