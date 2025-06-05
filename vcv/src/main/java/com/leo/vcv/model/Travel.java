package com.leo.vcv.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "travels")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "travel_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_id")
    private Long id;

    @NotBlank(message = "Destination cannot be blank")
    @Size(max = 100, message = "Destination must be at most 100 characters")
    private String destination;

    @FutureOrPresent(message = "Departure date must be in the present or future")
    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;

    @Future(message = "Return date must be in the future")
    @Column(name = "return_date", nullable = false)
    private LocalDate returnDate;

    @Positive(message = "Price must be positive")
    @Digits(integer = 10, fraction = 2, message = "Price must have up to 10 digits and 2 decimals")
    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @NotNull(message = "Client is required")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transport_id")
    private Transport transport;

    @Enumerated(EnumType.STRING)
    @Column(name = "travel_type", insertable = false, updatable = false)
    private TravelType travelType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "altered_at")
    private LocalDateTime alteredAt;

    public enum TravelType {
        NATIONAL,
        INTERNATIONAL
    }

    public boolean isInternational() {
        return this instanceof InternationalTravel;
    }

    public abstract String getTravelType();
}