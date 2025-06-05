package com.leo.vcv.service;

import com.leo.vcv.model.*;
import com.leo.vcv.repository.AccommodationRepository;
import com.leo.vcv.repository.ClientRepository;
import com.leo.vcv.repository.TransportRepository;
import com.leo.vcv.repository.TravelRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelService {

    private final TravelRepository travelRepository;
    private final ClientRepository clientRepository;
    private final TransportRepository transportRepository;
    private final AccommodationRepository accommodationRepository;

    private final NationalTravelStrategy nationalStrategy;
    private final InternationalTravelStrategy internationalStrategy;

    @Transactional
    public Travel createTravel(
            Travel.TravelType type,
            String destination,
            LocalDate departureDate,
            LocalDate returnDate,
            BigDecimal price,
            Long clientId,
            Long transportId,
            Long accommodationId,
            Boolean needsVisa
    ) {
        validateTravelDates(departureDate, returnDate);

        Travel travel = getStrategy(type).createTravel(
                destination,
                departureDate,
                returnDate,
                price,
                clientId,
                transportId,
                accommodationId,
                needsVisa
        );

        getStrategy(type).validateSpecificRules(travel);
        return travelRepository.save(travel);
    }

    private TravelStrategy getStrategy(Travel.TravelType type) {
        return switch (type) {
            case NATIONAL -> nationalStrategy;
            case INTERNATIONAL -> internationalStrategy;
        };
    }

    @Transactional(readOnly = true)
    public Travel getTravelById(Long id) {
        return travelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Travel not found"));
    }

    @Transactional
    public Travel updateTravel(
            Long id,
            Travel.TravelType type,
            String destination,
            LocalDate departureDate,
            LocalDate returnDate,
            BigDecimal price,
            Long clientId,
            Long transportId,
            Long accommodationId,
            Boolean needsVisa
    ) {
        Travel existing = getTravelById(id);
        validateTravelDates(departureDate, returnDate);

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        Transport transport = transportId != null ?
                transportRepository.findById(transportId).orElse(null) : null;

        Accommodation accommodation = accommodationId != null ?
                accommodationRepository.findById(accommodationId).orElse(null) : null;

        getStrategy(type).validateSpecificRules(existing);

        existing.setDestination(destination);
        existing.setDepartureDate(departureDate);
        existing.setReturnDate(returnDate);
        existing.setPrice(price);
        existing.setClient(client);
        existing.setTransport(transport);
        existing.setAccommodation(accommodation);
        existing.setAlteredAt(LocalDateTime.now());

        if (type == Travel.TravelType.INTERNATIONAL && existing instanceof InternationalTravel international) {
            international.setNeedsVisa(needsVisa);
        }

        return travelRepository.save(existing);
    }

    @Transactional(readOnly = true)
    public List<Travel> getAllTravels() {
        return travelRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Travel> getAllNationalTravels() {
        return travelRepository.findAllByTravelType(Travel.TravelType.NATIONAL);
    }

    @Transactional(readOnly = true)
    public List<Travel> getAllInternationalTravels() {
        return travelRepository.findAllByTravelType(Travel.TravelType.INTERNATIONAL);
    }

    @Transactional
    public void deleteTravel(Long id) {
        Travel travel = getTravelById(id);
        travelRepository.delete(travel);
    }

    private void validateTravelDates(LocalDate departureDate, LocalDate returnDate) {
        if (departureDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Departure date must be in the future or present");
        }
        if (returnDate.isBefore(departureDate)) {
            throw new IllegalArgumentException("Return date must be after departure date");
        }
    }
}