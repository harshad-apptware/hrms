package com.apptware.hrms.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

  @Query("SELECT c FROM Client c WHERE c.clientName LIKE %:clientName%")
  Client findByName(@Param("clientName") String name);

}
