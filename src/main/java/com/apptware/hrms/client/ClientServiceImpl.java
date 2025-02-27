package com.apptware.hrms.client;

import com.apptware.hrms.model.AddClientRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class ClientServiceImpl implements ClientService {

  @Autowired
  ClientRepository clientRepository;

  @Override
  public String saveClient(AddClientRequest clientRequest) {
    Client newClient = Client.builder().clientName(clientRequest.clientName())
        .location(clientRequest.location()).build();
    clientRepository.save(newClient);
    return "Client Created.";
  }

  @Override
  public Client fetchClientByName(String name) {
    return clientRepository.findByName(name);
  }

  @Override
  public Client fetchClientById(long clientId) {
    Optional<Client> optionalClient = clientRepository.findById(clientId);

    if (optionalClient.isPresent()) {
      return optionalClient.get();
    } else {
      throw new IllegalArgumentException("Invalid Client Id.");
    }
  }

  @Override
  public List<Client> fetchAllClients() {
    return clientRepository.findAll();
  }
}
