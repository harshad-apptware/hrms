package com.apptware.hrms.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

  Optional<Client> findByClientNameIgnoreCase(String name);

  Optional<Client> findByClientEmailIgnoreCase(String email);

}
