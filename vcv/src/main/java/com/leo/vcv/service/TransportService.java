package com.leo.vcv.service;

import com.leo.vcv.model.Transport;
import com.leo.vcv.repository.TransportRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransportService {

    private final TransportRepository transportRepository;

    public TransportService(TransportRepository transportRepository) {
        this.transportRepository = transportRepository;
    }

    @Transactional
    public Transport createTransport(Transport.TransportType type,
                                     String company,
                                     String number,
                                     Transport.ClassType classType) {

        Transport transport = Transport.builder()
                .type(type)
                .company(company)
                .number(number)
                .classType(classType != null ? classType : Transport.ClassType.ECONOMY)
                .build();

        return transportRepository.save(transport);
    }

    @Transactional(readOnly = true)
    public Transport getTransportById(Long id) {
        return transportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transporte não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Transport> getAllTransports() {
        return transportRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Transport> getTransportsByType(Transport.TransportType type) {
        return transportRepository.findByType(type);
    }

    @Transactional
    public Transport updateTransport(Long id,
                                     String company,
                                     String number,
                                     Transport.ClassType classType) {

        Transport transport = getTransportById(id);

        if (company != null) {
            transport.setCompany(company);
        }
        if (number != null) {
            transport.setNumber(number);
        }
        if (classType != null) {
            transport.setClassType(classType);
        }

        return transportRepository.save(transport);
    }

    @Transactional
    public void deleteTransport(Long id) {
        if (!transportRepository.existsById(id)) {
            throw new EntityNotFoundException("Transporte não encontrado");
        }
        transportRepository.deleteById(id);
    }
}
