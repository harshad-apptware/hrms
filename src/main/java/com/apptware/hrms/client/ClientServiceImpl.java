package com.apptware.hrms.client;

import com.apptware.hrms.model.ClientRequest;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class ClientServiceImpl implements ClientService {

  @Autowired
  ClientRepository clientRepository;

  @Override
  public String saveClient(ClientRequest clientRequest) {
    Optional<Client> byName = clientRepository.findByClientNameIgnoreCase(clientRequest.clientName());
    Optional<Client> byClientEmail = clientRepository.findByClientEmailIgnoreCase(clientRequest.clientEmail());
    if(byName.isPresent() && byClientEmail.isPresent()){
      return "Client already exists";
    }

    Client newClient =
            Client.builder()
                    .clientName(clientRequest.clientName())
                    .clientContact(clientRequest.clientContact())
                    .authorizedSignatory(clientRequest.authorizedSignatory())
                    .clientEmail(clientRequest.clientEmail())
                    .contactNo(clientRequest.contactNo())
                    .location(clientRequest.location())
                    .build();
    clientRepository.save(newClient);
    return "Client Created.";
  }

  @Override
  public Optional<Client> fetchClientByName(String name) {
    return clientRepository.findByClientNameIgnoreCase(name.toLowerCase());
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

  @Override
  public String updateClientDetails(Client client) {
    Client newClient = fetchClientById(client.getId());
    newClient.setClientName(client.getClientName());
    newClient.setClientContact(client.getClientContact());
    newClient.setAuthorizedSignatory(client.getAuthorizedSignatory());
    newClient.setClientEmail(client.getClientEmail());
    newClient.setContactNo(client.getContactNo());
    newClient.setLocation(client.getLocation());
    clientRepository.save(newClient);
    return "Client updated successfully";
  }

  @Override
  public String deleteClient(long id) {
    Optional<Client> optionalClient = clientRepository.findById(id);
    if(optionalClient.isPresent()){
      clientRepository.deleteById(id);
      return "Client deleted";
    }
    return "Client not found";
  }
}
