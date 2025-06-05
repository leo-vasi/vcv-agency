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
public class InternationalTravelStrategy implements TravelStrategy {

    private final ClientRepository clientRepository;
    private final TransportRepository transportRepository;
    private final AccommodationRepository accommodationRepository;

    @Override
    public Travel createTravel(String destination, LocalDate departureDate,
                               LocalDate returnDate, BigDecimal price, Long clientId,
                               Long transportId, Long accommodationId, Boolean needsVisa) {

        if (needsVisa == null) {
            throw new IllegalArgumentException("Visa is required for international travels");
        }

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        Transport transport = transportId != null ?
                transportRepository.findById(transportId).orElse(null) : null;

        Accommodation accommodation = accommodationId != null ?
                accommodationRepository.findById(accommodationId).orElse(null) : null;

        return TravelFactory.createInternationalTravel(
                destination,
                departureDate,
                returnDate,
                price,
                client,
                transport,
                accommodation,
                needsVisa
        );
    }

    @Override
    public void validateSpecificRules(Travel travel) {
        if (travel.getPrice().compareTo(new BigDecimal("500")) < 0) {
            throw new IllegalArgumentException("International travels must cost at least 500");
        }
    }
}
