package com.apptware.hrms.client;

import com.apptware.hrms.model.ClientRequest;
import java.util.List;
import java.util.Optional;

interface ClientService {

  String  saveClient(ClientRequest clientRequest);

  Optional<Client> fetchClientByName(String name);

  Client fetchClientById(long clientId);

  List<Client> fetchAllClients();

  String updateClientDetails(Client client);

  String deleteClient(long id);
}
