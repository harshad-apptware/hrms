package com.apptware.hrms.project;

import com.apptware.hrms.model.AddProjectRequest;
import com.apptware.hrms.project.Project.ProjectStatus;
import java.util.List;

interface ProjectService {

  String saveProject(AddProjectRequest projectRequest);

  Project fetchProjectById(long projectId);

  List<Project> fetchProjectsByClientId(long clientId);

  String updateProjectStatus(long projectId, ProjectStatus projectStatus);

  List<Project> fetchAllProjects();
}
