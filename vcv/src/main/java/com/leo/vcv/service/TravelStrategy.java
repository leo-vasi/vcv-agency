package com.leo.vcv.service;

import com.leo.vcv.model.Travel;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface TravelStrategy {
    Travel createTravel(
            String destination,
            LocalDate departureDate,
            LocalDate returnDate,
            BigDecimal price,
            Long clientId,
            Long transportId,
            Long accommodationId,
            Boolean needsVisa
    );

    void validateSpecificRules(Travel travel);
}
