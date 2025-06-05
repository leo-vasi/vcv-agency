package com.leo.vcv.service;

import com.leo.vcv.model.*;
import com.leo.vcv.repository.AccommodationRepository;
import com.leo.vcv.repository.ClientRepository;
import com.leo.vcv.repository.TransportRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class NationalTravelStrategy implements TravelStrategy {

    private final ClientRepository clientRepository;
    private final TransportRepository transportRepository;
    private final AccommodationRepository accommodationRepository;

    @Override
    public Travel createTravel(String destination, LocalDate departureDate,
                               LocalDate returnDate, BigDecimal price, Long clientId,
                               Long transportId, Long accommodationId, Boolean needsVisa) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        Transport transport = transportId != null ?
                transportRepository.findById(transportId).orElse(null) : null;

        Accommodation accommodation = accommodationId != null ?
                accommodationRepository.findById(accommodationId).orElse(null) : null;

        return TravelFactory.createNationalTravel(
                destination,
                departureDate,
                returnDate,
                price,
                client,
                transport,
                accommodation
        );
    }

    @Override
    public void validateSpecificRules(Travel travel) {
        if (travel.getPrice().compareTo(new BigDecimal("10000")) > 0) {
            throw new IllegalArgumentException("National travels cannot cost more than 10000");
        }
    }
}