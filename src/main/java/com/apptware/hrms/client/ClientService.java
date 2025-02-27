package com.apptware.hrms.client;

import com.apptware.hrms.model.AddClientRequest;
import java.util.List;

interface ClientService {

  String  saveClient(AddClientRequest clientRequest);

  Client fetchClientByName(String name);

  Client fetchClientById(long clientId);

  List<Client> fetchAllClients();
}
