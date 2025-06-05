package com.leo.vcv.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "transports")
@Getter
@Setter
@NoArgsConstructor
public class Transport {

    public enum TransportType { AIR, BUS, SHIP }
    public enum ClassType { ECONOMY, EXECUTIVE, FIRST_CLASS }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transport_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "transport_type", nullable = false, columnDefinition = "ENUM('AIR', 'BUS', 'SHIP')")
    @NotNull(message = "Transport type is mandatory")
    private TransportType type;

    @Size(max = 100, message = "Company name must be at most 100 characters")
    private String company;

    @Size(max = 50, message = "Transport number must be at most 50 characters")
    @Column(name = "transport_number")
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(name = "class_type", columnDefinition = "ENUM('ECONOMY', 'EXECUTIVE', 'FIRST_CLASS') DEFAULT 'ECONOMY'")
    private ClassType classType = ClassType.ECONOMY;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "altered_at")
    private LocalDateTime alteredAt;


    public static TransportBuilder builder() {
        return new TransportBuilder();
    }

    public static class TransportBuilder {
        private final Transport transport = new Transport();

        public TransportBuilder type(TransportType type) {
            transport.setType(type);
            return this;
        }

        public TransportBuilder company(String company) {
            transport.setCompany(company);
            return this;
        }

        public TransportBuilder number(String number) {
            transport.setNumber(number);
            return this;
        }

        public TransportBuilder classType(ClassType classType) {
            transport.setClassType(classType);
            return this;
        }

        public Transport build() {
            if (transport.getType() == null) {
                throw new IllegalStateException("Transport type is required");
            }
            return transport;
        }
    }

}
