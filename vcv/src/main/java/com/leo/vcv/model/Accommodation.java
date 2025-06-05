package com.leo.vcv.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accommodations")
@Getter
@Setter
@NoArgsConstructor
public class Accommodation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accommodation_id")
    private Long id;

    @NotBlank(message = "Hotel name is mandatory")
    @Size(max = 100, message = "Hotel name must be at most 100 characters")
    @Column(name = "hotel_name", nullable = false, length = 100)
    private String hotelName;

    @Size(max = 200, message = "Address must be at most 200 characters")
    private String address;

    @Min(value = 1, message = "Category must be at least 1")
    @Max(value = 5, message = "Category must be at most 5")
    private Integer category;

    @Column(name = "has_breakfast", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean hasBreakfast = false;

    @Column(name = "has_pool", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean hasPool = false;

    @Column(name = "has_wifi", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean hasWifi = false;

    @PositiveOrZero(message = "Daily rate must be positive or zero")
    @Digits(integer = 10, fraction = 2, message = "Daily rate must have up to 10 digits and 2 decimals")
    @Column(name = "daily_rate", precision = 10, scale = 2)
    private BigDecimal dailyRate;

    @PositiveOrZero(message = "Room count must be positive or zero")
    @Column(name = "room_count")
    private Integer roomCount;

    @Lob
    private String notes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "altered_at")
    private LocalDateTime alteredAt;

    public static AccommodationBuilder builder() {
        return new AccommodationBuilder();
    }

    public static class AccommodationBuilder {
        private final Accommodation accommodation = new Accommodation();

        public AccommodationBuilder hotelName(String hotelName) {
            accommodation.setHotelName(hotelName);
            return this;
        }

        public AccommodationBuilder address(String address) {
            accommodation.setAddress(address);
            return this;
        }

        public AccommodationBuilder category(Integer category) {
            accommodation.setCategory(category);
            return this;
        }

        public AccommodationBuilder dailyRate(BigDecimal dailyRate) {
            accommodation.setDailyRate(dailyRate);
            return this;
        }

        public AccommodationBuilder roomCount(Integer roomCount) {
            accommodation.setRoomCount(roomCount);
            return this;
        }

        public Accommodation build() {
            if (accommodation.getHotelName() == null || accommodation.getHotelName().trim().isEmpty()) {
                throw new IllegalStateException("Hotel name is required");
            }
            return accommodation;
        }
    }

}