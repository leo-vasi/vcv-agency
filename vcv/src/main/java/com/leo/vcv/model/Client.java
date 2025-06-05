package com.leo.vcv.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long id;

    @NotBlank(message = "Client name is mandatory")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(name = "client_name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Pattern(regexp = "^\\+?[0-9\\s-]{10,20}$", message = "Invalid phone number format")
    @Column(length = 20)
    private String phone;

    @Pattern(regexp = "^[A-Za-z0-9]{6,10}$", message = "O número do passaporte deve conter entre 6 e 10 caracteres alfanuméricos")
    @Column(name = "passport_number", length = 10)
    private String passportNumber;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "altered_at")
    private LocalDateTime alteredAt;



    public static ClientBuilder builder() {
        return new ClientBuilder();
    }

    public static class ClientBuilder {
        private final Client client = new Client();

        public ClientBuilder name(String name) {
            client.setName(name);
            return this;
        }

        public ClientBuilder email(String email) {
            client.setEmail(email);
            return this;
        }

        public ClientBuilder phone(String phone) {
            client.setPhone(phone);
            return this;
        }

        public Client build() {
            if (client.getName() == null || client.getEmail() == null) {
                throw new IllegalStateException("Name and email are required");
            }
            return client;
        }
    }
}
