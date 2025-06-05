CREATE DATABASE vcv_db;
USE vcv_db;


CREATE TABLE accommodations (
    accommodation_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    hotel_name VARCHAR(100) NOT NULL,
    address VARCHAR(200),
    category INT CHECK (category BETWEEN 1 AND 5),
    has_breakfast BOOLEAN DEFAULT FALSE,
    has_pool BOOLEAN DEFAULT FALSE,
    has_wifi BOOLEAN DEFAULT FALSE,
    daily_rate DECIMAL(10,2),
    room_count INT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    altered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE clients (
    client_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    client_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    passport_number VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    altered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE transports (
    transport_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    transport_type ENUM('AIR', 'BUS', 'SHIP') NOT NULL,
    company VARCHAR(100),
    transport_number VARCHAR(50),
    class_type ENUM('ECONOMY', 'EXECUTIVE', 'FIRST_CLASS') DEFAULT 'ECONOMY',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    altered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE travels (
    travel_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    destination VARCHAR(100) NOT NULL,
    departure_date DATE NOT NULL,
    return_date DATE NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    travel_type ENUM('NATIONAL', 'INTERNATIONAL') NOT NULL,
    needs_visa BOOLEAN DEFAULT FALSE,
    client_id BIGINT NOT NULL,
    transport_id BIGINT,
    accommodation_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    altered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_travel_client FOREIGN KEY (client_id) REFERENCES clients(client_id) ON DELETE CASCADE,
    CONSTRAINT fk_travel_transport FOREIGN KEY (transport_id) REFERENCES transports(transport_id) ON DELETE SET NULL,
    CONSTRAINT fk_travel_accommodation FOREIGN KEY (accommodation_id) REFERENCES accommodations(accommodation_id) ON DELETE SET NULL
);

-- Inserções para clients
INSERT INTO clients (client_name, email, phone, passport_number) VALUES 
  ('Fernandinho Beira-Mar', 'fernando.bm@example.com', '2199999-1111', 'BR123456'),
  ('Marcola', 'marcola@example.com', '1198888-2222', 'BR654321'),
  ('Elize Matsunaga', 'matsunaga@exemple.com', '1190098-2121', 'JP789012'),
  ('André do Rap', 'andre.rap@example.com', '1397777-3333', 'BR345678'),
  ('Rogério 157', 'rogerio157@example.com', '2196666-4444', 'BR876543'),
  ('Gegê do Mangue', 'gege.mangue@example.com', '8595555-5555', 'BR567890'),
  ('Suzane von Richthofen', 'suz.von@example.com', '1194515-5157', 'DE123456'),
  ('Pedrinho Matador', 'matador@example.com', '1194665-5160', 'BR098765');

-- Inserções para transports
INSERT INTO transports (transport_type, company, transport_number, class_type) VALUES 
  ('AIR', 'LATAM', 'LA1234', 'ECONOMY'),
  ('AIR', 'GOL', 'GOL5678', 'EXECUTIVE'),
  ('AIR', 'Azul', 'AZU9012', 'FIRST_CLASS'),
  ('BUS', 'Itapemirim', 'IT456', 'ECONOMY'),
  ('BUS', 'Cometa', 'COM789', 'EXECUTIVE'),
  ('SHIP', 'MSC Cruzeiros', 'MSC789', 'FIRST_CLASS'),
  ('SHIP', 'Costa Cruzeiros', 'CST123', 'EXECUTIVE');

-- Inserções para accommodations
INSERT INTO accommodations (
  hotel_name, address, category, has_breakfast, has_pool, has_wifi, daily_rate, room_count, notes) VALUES 
  ('Copacabana Palace', 'Av. Atlântica, 1702 - Rio de Janeiro', 5, TRUE, TRUE, TRUE, 1500.00, 120, 'Vista para o mar'),
  ('Hotel Fasano', 'Rua Vittorio Fasano, 88 - São Paulo', 5, TRUE, TRUE, TRUE, 1800.00, 80, 'Hotel de luxo'),
  ('Hotel Fazenda Boa Vista', 'Rod. Castello Branco, km 102 - Porto Feliz', 4, TRUE, TRUE, FALSE, 800.00, 40, 'Ideal para relaxar'),
  ('Pousada do Sossego', 'Rua das Palmeiras, 45 - Paraty', 3, TRUE, FALSE, TRUE, 300.00, 10, 'Ambiente familiar'),
  ('Grand Hyatt', 'Av. das Nações Unidas, 13301 - São Paulo', 5, TRUE, TRUE, TRUE, 1200.00, 200, 'Centro de convenções');

INSERT INTO travels (
    destination, 
    departure_date, 
    return_date, 
    price, 
    travel_type,
    client_id, 
    transport_id, 
    accommodation_id,
    needs_visa
) VALUES 
  ('Cancún', '2025-07-10', '2025-07-20', 8500.00, 'INTERNATIONAL', 1, 1, 1, TRUE),
  ('Foz do Iguaçu', '2025-06-01', '2025-06-07', 2500.00, 'NATIONAL', 2, 4, 3, FALSE),
  ('Ilhabela', '2025-12-22', '2025-12-27', 1900.00, 'NATIONAL', 3, 5, 4, FALSE),
  ('Lisboa', '2025-09-15', '2025-09-30', 9200.00, 'INTERNATIONAL', 4, 2, 2, TRUE),
  ('Natal (RN)', '2025-08-05', '2025-08-12', 3000.00, 'NATIONAL', 5, 4, 4, FALSE),
  ('Miami', '2025-11-10', '2025-11-20', 7500.00, 'INTERNATIONAL', 6, 3, 1, TRUE),
  ('Gramado', '2025-07-15', '2025-07-22', 2200.00, 'NATIONAL', 7, 5, 3, FALSE),
  ('Paris', '2025-10-01', '2025-10-15', 10500.00, 'INTERNATIONAL', 8, 1, 5, TRUE);


SELECT * FROM clients;
SELECT * FROM transports;
SELECT * FROM accommodations;
SELECT * FROM travels;
