package com.apptware.hrms.project;

import com.apptware.hrms.model.ProjectRequest;
import com.apptware.hrms.project.Project.ProjectStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/project")
@Tag(name = "Project APIs", description = "Create, Read & Update Project")
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
    if(saved.equals("Project added")) {
      return ResponseEntity.ok(saved);
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(saved);
  }

  @PatchMapping("/{projectId}/updateStatus")
  ResponseEntity<String> updateProjectStatus(@PathVariable long projectId, @RequestParam
      ProjectStatus status) {
    String projectStatus = projectService.updateProjectStatus(projectId, status);
    return ResponseEntity.ok(projectStatus);
  }

  @DeleteMapping("/delete")
  ResponseEntity<String> deleteProject(@RequestParam Long id){
    String message = projectService.deleteProject(id);
    if(message.equals("Project deleted")){
      return ResponseEntity.status(HttpStatus.OK).body(message);
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
  }

  @PutMapping("/update")
  ResponseEntity<String> updateProject(@RequestParam Long id, @RequestBody ProjectRequest projectRequest){
    String message = projectService.updateProject(id, projectRequest);
    if(message.equals("Project updated")){
      return ResponseEntity.ok(message);
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
  }
}
