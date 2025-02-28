package com.apptware.hrms.client;

import com.apptware.hrms.model.ClientRequest;
import java.util.List;

interface ClientService {

  String  saveClient(ClientRequest clientRequest);

  Client fetchClientByName(String name);

  Client fetchClientById(long clientId);

  List<Client> fetchAllClients();

  String updateClientDetails(Client client);
}
