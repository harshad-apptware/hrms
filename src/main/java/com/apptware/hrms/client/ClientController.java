package com.apptware.hrms.client;

import com.apptware.hrms.model.ClientRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin
@RestController
@RequestMapping("/client")
@Tag(name = "Client APIs", description = "Create, Read & Update Client")
public class ClientController {

  @Autowired
  ClientService clientService;

  @GetMapping("/byId")
  ResponseEntity<?> getClientById(@RequestParam long id) {
    Optional<Client> optionalClient = clientService.fetchClientById(id);
    if (optionalClient.isPresent()) {
      return ResponseEntity.ok(optionalClient.get());
    } else {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Invalid Client Id.");
    }
  }

  @GetMapping("/byName") //need to check if client not present in DB returning noting and 200OK
  ResponseEntity<?> getClientByName(@RequestParam String clientName) {
    Optional<Client> optionalClient = clientService.fetchClientByName(clientName);
    if(optionalClient.isPresent()){
      return ResponseEntity.status(HttpStatus.OK).body(optionalClient.get());
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
  }

  @GetMapping("/listClients")
  ResponseEntity<List<Client>> getListOfClients() {
    List<Client> clients = clientService.fetchAllClients();
    return ResponseEntity.ok(clients);
  }

  @PostMapping("/add")
  ResponseEntity<String> addNewClient(@RequestBody ClientRequest clientRequest) {
    String saved = clientService.saveClient(clientRequest);
    if (saved.equals("Client already exists")){
      return ResponseEntity.status(HttpStatus.CONFLICT).body(saved);
    }
    return ResponseEntity.ok(saved);
  }

  @PutMapping("/update")
  ResponseEntity<String> updateClientDetails(@RequestBody Client client){
    String message = clientService.updateClientDetails(client);
    if(message.equals("Client updated")){
      return ResponseEntity.status(HttpStatus.ACCEPTED).body(message);
    }
    return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
  }

  @DeleteMapping("/delete")
  ResponseEntity<String> deleteClient(@RequestParam Long id){
    String message = clientService.deleteClient(id);
    if("Client deleted".equals(message)){
      return ResponseEntity.status(HttpStatus.OK).body(message);
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
  }
}
