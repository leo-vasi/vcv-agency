package com.leo.vcv.service;

import com.leo.vcv.model.Accommodation;
import com.leo.vcv.repository.AccommodationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;

    public AccommodationService(AccommodationRepository accommodationRepository) {
        this.accommodationRepository = accommodationRepository;
    }

    @Transactional
    public Accommodation createAccommodation(
            String hotelName,
            String address,
            Integer category,
            BigDecimal dailyRate,
            Integer roomCount,
            Boolean hasBreakfast,
            Boolean hasPool,
            Boolean hasWifi,
            String notes) {

        Accommodation accommodation = Accommodation.builder()
                .hotelName(hotelName)
                .address(address)
                .category(category)
                .dailyRate(dailyRate)
                .roomCount(roomCount)
                .build();

        accommodation.setHasBreakfast(hasBreakfast != null ? hasBreakfast : false);
        accommodation.setHasPool(hasPool != null ? hasPool : false);
        accommodation.setHasWifi(hasWifi != null ? hasWifi : false);
        accommodation.setNotes(notes);

        validateAccommodation(accommodation);
        return accommodationRepository.save(accommodation);
    }

    @Transactional(readOnly = true)
    public Accommodation getAccommodationById(Long id) {
        return accommodationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Accommodation not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Accommodation> getAllAccommodations() {
        return accommodationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Accommodation> searchAccommodations(
            String hotelName,
            Integer minCategory,
            BigDecimal maxDailyRate,
            Boolean hasBreakfast,
            Boolean hasPool,
            Boolean hasWifi) {

        return accommodationRepository.findByCriteria(
                hotelName,
                minCategory,
                maxDailyRate,
                hasBreakfast,
                hasPool,
                hasWifi);
    }

    @Transactional
    public Accommodation updateAccommodation(Long id, Accommodation updatedData) {
        Accommodation existing = getAccommodationById(id);

        if (updatedData.getHotelName() != null) {
            existing.setHotelName(updatedData.getHotelName());
        }
        if (updatedData.getAddress() != null) {
            existing.setAddress(updatedData.getAddress());
        }
        if (updatedData.getCategory() != null) {
            existing.setCategory(updatedData.getCategory());
        }
        if (updatedData.getDailyRate() != null) {
            existing.setDailyRate(updatedData.getDailyRate());
        }
        if (updatedData.getRoomCount() != null) {
            existing.setRoomCount(updatedData.getRoomCount());
        }
        if (updatedData.getHasBreakfast() != null) {
            existing.setHasBreakfast(updatedData.getHasBreakfast());
        }
        if (updatedData.getHasPool() != null) {
            existing.setHasPool(updatedData.getHasPool());
        }
        if (updatedData.getHasWifi() != null) {
            existing.setHasWifi(updatedData.getHasWifi());
        }
        if (updatedData.getNotes() != null) {
            existing.setNotes(updatedData.getNotes());
        }

        existing.setAlteredAt(LocalDateTime.now());
        validateAccommodation(existing);
        return accommodationRepository.save(existing);
    }

    @Transactional
    public void deleteAccommodation(Long id) {
        if (!accommodationRepository.existsById(id)) {
            throw new EntityNotFoundException("Accommodation not found with id: " + id);
        }
        accommodationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Accommodation> findLuxuryAccommodations() {
        return accommodationRepository.findByCategoryGreaterThanEqual(4);
    }

    private void validateAccommodation(Accommodation accommodation) {
        if (accommodation.getCategory() != null && accommodation.getCategory() == 5
                && accommodation.getDailyRate().compareTo(new BigDecimal("500")) < 0) {
            throw new IllegalArgumentException("5-star accommodations must have daily rate of at least 500");
        }

        if (accommodation.getRoomCount() != null && accommodation.getRoomCount() == 0
                && accommodation.getDailyRate().compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalArgumentException("Accommodation with no rooms cannot have positive daily rate");
        }
    }
}