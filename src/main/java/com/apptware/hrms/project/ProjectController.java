package com.apptware.hrms.project;

import com.apptware.hrms.model.ProjectRequest;
import com.apptware.hrms.project.Project.ProjectStatus;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/project")
public class ProjectController {

  @Autowired
  ProjectService projectService;

  @GetMapping("/listProjects")
  ResponseEntity<List<Project>> getAllProjects() {
    List<Project> projects = projectService.fetchAllProjects();
    return ResponseEntity.ok(projects);
  }

  @GetMapping("/byId")
  ResponseEntity<Project> getProjectById(@RequestParam long projectId) {
    Project project = projectService.fetchProjectById(projectId);
    return ResponseEntity.ok(project);
  }

  @GetMapping("byClientId")
  ResponseEntity<List<Project>> getProjectByClientId(@RequestParam long clientId) {
    List<Project> projects = projectService.fetchProjectsByClientId(clientId);
    return ResponseEntity.ok(projects);
  }

  @PostMapping("/add")
  ResponseEntity<String> addNewProject(@RequestBody ProjectRequest projectRequest) {
    String saved = projectService.saveProject(projectRequest);
    return ResponseEntity.ok(saved);
  }

  @PatchMapping("/{projectId}/updateStatus")
  ResponseEntity<String> updateProjectStatus(@PathVariable long projectId, @RequestParam
      ProjectStatus status) {
    String projectStatus = projectService.updateProjectStatus(projectId, status);
    return ResponseEntity.ok(projectStatus);
  }
}
