# VCV - CRUD com Design Patterns + Spring

## 📜 Descrição

Sistema de gestão de viagens (nacionais e internacionais) com **Spring Boot**, **JPA** e **Thymeleaf**.

---

## ⚙️ Funcionalidades

- CRUD para:
  - **Clientes** (`Client`)
  - **Transportes** (`Transport`)
  - **Acomodações** (`Accommodation`)
  - **Viagens** (`Travel` e subclasses)
- Validações específicas por tipo de viagem (ex.: preço mínimo/máximo, datas)
- Filtros para viagens nacionais e internacionais
- Mensagens de sucesso/erro dinâmicas

---

## 🎯 Objetivo Didático

- **Factory Method**: Criação flexível de viagens
- **Strategy Pattern**: Regras de negócio especializadas
- **Facade Pattern**: Simplificação da API de serviços
- **Template Method**: Reuso de lógica em controllers
- Herança (`Travel` como superclasse abstrata) e polimorfismo

---

## 🏗️ Modelo de Dados

| Entidade             | Descrição                                   | Relacionamentos                           |
| -------------------- | ------------------------------------------- | ------------------------------------------ |
| `Client`             | Cliente que realiza viagens                | Relacionado com `Travel`                  |
| `Transport`          | Meio de transporte (voo, ônibus)           | Relacionado com `Travel`                  |
| `Accommodation`      | Hospedagem durante a viagem                | Relacionado com `Travel`                  |
| `Travel` (abstract)  | Superclasse abstrata de viagens            | Pai de `InternationalTravel` e `NationalTravel` |
| `InternationalTravel`| Viagem internacional                       | Herda `Travel` + atributo `needsVisa`     |
| `NationalTravel`     | Viagem nacional                            | Herda `Travel`                            |

---

## ✨ Destaques de Design Patterns

### 1️⃣ Factory Method
- **Classe:** `TravelFactory`
- **Finalidade:** Centralizar a criação complexa de viagens.

---

### 2️⃣ Strategy Pattern (Camada Service)
- **Interface:** `TravelStrategy`
- **Implementações:**
  - `InternationalTravelStrategy`: Valida preço mínimo (≥ 500) e exigência de visto.
  - `NationalTravelStrategy`: Valida preço máximo (≤ 10.000).

---

### 3️⃣ Facade Pattern
- **Classe:** `TravelService`
- **Finalidade:** Unificar operações complexas em uma interface simplificada.

---

### 4️⃣ Template Method Pattern (Camada Controller)
- **Classe:** `TravelController`
- **Métodos-chave:**
  - `showCreateForm()`: Prepara modelo comum para formulários.
  - `processCreateTravel()`: Lógica compartilhada para criação de viagens.
  - `showEditForm()`: Reutilizado para edição de ambos os tipos.
- **Vantagem:** Elimina duplicação de código em endpoints similares.

```java
// Exemplo de Template Method em ação:
@GetMapping("/national/create")
public String showCreateNationalForm(Model model) {
    return showCreateForm(model, "national"); // Reutiliza lógica base
}
```

## 🛠️ Tecnologias Utilizadas

### Backend:
- Java 21
- Spring Boot 3.x
- Spring Data JPA + Hibernate
- Lombok (`@RequiredArgsConstructor`, `@Builder`, `@Getter`, `@Setter` etc)

### Frontend:
- Thymeleaf
- HTML + Bootstrap (uso básico)

### Banco de Dados:
- MySQL 8.0+

