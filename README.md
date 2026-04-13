✈️ Wigell Travels – Travel Booking Microservice

A Spring Boot microservice for managing customers, destinations, and travel bookings. Part of a microservice architecture with Keycloak-based authentication and authorization.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [API Endpoints](#api-endpoints)
- [Security & Roles](#security--roles)
- [Database Seeding](#database-seeding)
- [Getting Started](#getting-started)
- [Configuration](#configuration)

---

## Overview

Wigell Travels handles the core travel booking domain:

- **Customer management** – Create, update, and delete customers with linked Keycloak accounts
- **Destination management** – CRUD for travel destinations with per-week pricing
- **Booking management** – Create and patch bookings with automatic SEK → PLN currency conversion via an external currency service

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Framework | Spring Boot |
| Security | Spring Security + Keycloak (JWT/OAuth2) |
| Persistence | Spring Data JPA + Hibernate |
| Database | MySQL |
| Currency | External CurrencyClient (Feign/REST) |
| Mapping | Custom MapStruct-style mappers |
| Logging | SLF4J |

---

## Architecture
Controller → Service → Repository → MySQL
↓
KeycloakUserService → Keycloak Admin REST API
↓
CurrencyClient → External Currency Service

**Seeders run at startup (in order):**
1. `CustomerUserAddressSeeder` – Seeds 5 customers with Keycloak accounts and addresses
2. `DestinationSeeder` – Seeds 5 travel destinations

---

## API Endpoints

### Customers `/api/v1/customers`
> All customer endpoints require `ADMIN` role

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/` | List all customers |
| `POST` | `/` | Create customer + Keycloak user |
| `PUT` | `/{id}` | Update customer details |
| `DELETE` | `/{id}` | Delete customer (blocked if active bookings exist) |
| `POST` | `/{customerId}/addresses` | Add address to customer |
| `DELETE` | `/{customerId}/addresses/{addressId}` | Remove address from customer |

### Destinations `/api/v1/destinations`

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| `GET` | `/` | `USER` or `ADMIN` | List all destinations |
| `POST` | `/` | `ADMIN` | Create destination |
| `PUT` | `/{id}` | `ADMIN` | Update destination |
| `DELETE` | `/{id}` | `ADMIN` | Delete destination |

### Bookings `/api/v1/bookings`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/{customerId}` | List bookings for a customer |
| `POST` | `/` | Create booking (auto-converts price SEK → PLN) |
| `PATCH` | `/{bookingId}` | Patch booking (recalculates price if needed) |

---

## Security & Roles

Authentication is handled via **Keycloak** using JWT bearer tokens. Two roles are used:

- **`USER`** – Can view destinations and manage own bookings
- **`ADMIN`** – Full access to all endpoints including customer and destination management

New users created via the API are automatically assigned the `USER` role in Keycloak.

---

## Database Seeding

On startup, if the database is empty, the following seed data is inserted:

**Customers (+ Keycloak accounts):**

| Name | City |
|------|------|
| Kalle Qvist | Uppsala |
| Christoffer Danielsson | Umeå |
| Sven Svensson | Stockholm |
| Anna Andersson | Göteborg |
| Lisa Lindgren | Malmö |

**Destinations:**

| Hotel | City | Country | Price/Week (SEK) |
|-------|------|---------|-----------------|
| Comfort Hotel | Umeå | Sweden | 2 999.99 |
| Grand Hotel | Stockholm | Sweden | 5 499.00 |
| Eiffel Views | Paris | France | 8 999.50 |
| Colosseum Resort | Rome | Italy | 7 500.00 |
| Alps Chalet | Innsbruck | Austria | 6 200.75 |

---

## Getting Started

### Prerequisites

- Java 21+
- MySQL
- Keycloak instance
- Running currency service

### Configuration

Set the following in `application.properties` or as environment variables:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/wigell_travel_db
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password

# Keycloak Admin
keycloak.admin.server-url=http://localhost:8080
keycloak.admin.realm=your-realm
keycloak.admin.client-id=your-client-id
keycloak.admin.client-secret=your-client-secret
```

### Run

```bash
./mvnw spring-boot:run
```

The service starts on port `8581` by default.

---
