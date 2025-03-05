package com.apptware.hrms.project;

import com.apptware.hrms.client.Client;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

  @Transactional
  @Query("SELECT p FROM Project p WHERE p.client.id = :clientId")
  List<Project> findProjectsByClientId(@Param("clientId") long clientId);

  Optional<Project> findByProjectNameIgnoreCase(String projectName);

}
