package com.leo.vcv.model;


import java.math.BigDecimal;
import java.time.LocalDate;

public class TravelFactory {

    public static NationalTravel createNationalTravel(
            String destination,
            LocalDate departureDate,
            LocalDate returnDate,
            BigDecimal price,
            Client client,
            Transport transport,
            Accommodation accommodation) {

        return NationalTravel.builder()
                .destination(destination)
                .departureDate(departureDate)
                .returnDate(returnDate)
                .price(price)
                .client(client)
                .transport(transport)
                .accommodation(accommodation)
                .build();
    }

    public static InternationalTravel createInternationalTravel(
            String destination,
            LocalDate departureDate,
            LocalDate returnDate,
            BigDecimal price,
            Client client,
            Transport transport,
            Accommodation accommodation,
            boolean needsVisa) {

        return InternationalTravel.builder()
                .destination(destination)
                .departureDate(departureDate)
                .returnDate(returnDate)
                .price(price)
                .client(client)
                .transport(transport)
                .accommodation(accommodation)
                .needsVisa(needsVisa)
                .build();
    }
}