package com.apptware.hrms.client;

import com.apptware.hrms.model.ClientRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin
@RestController
@RequestMapping("/client")
public class ClientController {

  @Autowired
  ClientService clientService;

  @PostMapping("/add")
  ResponseEntity<String> addNewClient(@RequestBody ClientRequest clientRequest) {
    String saved = clientService.saveClient(clientRequest);
    return ResponseEntity.ok(saved);
  }

  @GetMapping("/byId")
  ResponseEntity<Client> getClientById(@RequestParam long clientId) {
    Client client = clientService.fetchClientById(clientId);
    return ResponseEntity.ok(client);
  }

  @GetMapping("/byName")
  ResponseEntity<Client> getClientByName(@RequestParam String clientName) {
    Client client = clientService.fetchClientByName(clientName);
    return ResponseEntity.ok(client);
  }

  @GetMapping("/listClients")
  ResponseEntity<List<Client>> getListOfClients() {
    List<Client> clients = clientService.fetchAllClients();
    return ResponseEntity.ok(clients);
  }

  @PutMapping("/update")
  ResponseEntity<String> updateClientDetails(@RequestBody Client client){
    String message = clientService.updateClientDetails(client);
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(message);
  }
}
