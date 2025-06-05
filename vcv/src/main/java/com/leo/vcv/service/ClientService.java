package com.leo.vcv.service;

import com.leo.vcv.model.Client;
import com.leo.vcv.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional
    public Client createClient(String name, String email, String phone, String passportNumber) {
        Client client = Client.builder()
                .name(name)
                .email(email)
                .phone(phone)
                .build();

        client.setPassportNumber(passportNumber);
        validateClient(client);
        return clientRepository.save(client);
    }

    @Transactional(readOnly = true)
    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Transactional
    public Client updateClient(Long id, String name, String phone, String passportNumber) {
        Client client = getClientById(id);

        if (name != null) {
            client.setName(name);
        }
        if (phone != null) {
            client.setPhone(phone);
        }
        if (passportNumber != null) {
            client.setPassportNumber(passportNumber);
        }

        validateClient(client);
        return clientRepository.save(client);
    }

    @Transactional
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new EntityNotFoundException("Cliente não encontrado");
        }
        clientRepository.deleteById(id);
    }

    private void validateClient(Client client) {
        if (clientRepository.existsByEmailAndIdNot(client.getEmail(), client.getId() != null ? client.getId() : 0L)) {
            throw new IllegalArgumentException("E-mail já está em uso");
        }

        if (client.getPassportNumber() != null && !client.getPassportNumber().isEmpty()) {
            if (!client.getPassportNumber().matches("^[A-Za-z0-9]{6,10}$")) {
                throw new IllegalArgumentException("Número do passaporte deve conter 6 a 10 caracteres alfanuméricos");
            }
        }
    }
}