# Wallet Service

Wallet management microservice for the PayFlow fintech platform.

## Overview

The Wallet Service is responsible for managing user wallets, maintaining balances, and processing wallet operations such as credit, debit, and balance inquiries.

## Features

- Create Wallet
- Get Wallet Details
- Get Wallet By User ID
- Check Wallet Balance
- Credit Wallet
- Debit Wallet
- Freeze Wallet
- Activate Wallet
- Wallet Status Management
- RESTful APIs
- Validation and Exception Handling
- OpenAPI / Swagger Documentation
- Eureka Client Integration

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Data JPA
- PostgreSQL
- Spring Cloud Eureka Client
- OpenFeign
- Lombok
- MapStruct
- Maven
- Swagger OpenAPI

## API Endpoints

### Wallet Management

| Method | Endpoint | Description |
|----------|------------|-------------|
| POST | /api/v1/wallets | Create wallet |
| GET | /api/v1/wallets/{walletId} | Get wallet details |
| GET | /api/v1/wallets/user/{userId} | Get wallet by user |
| GET | /api/v1/wallets/balance/{walletId} | Get wallet balance |
| PUT | /api/v1/wallets/credit | Credit wallet |
| PUT | /api/v1/wallets/debit | Debit wallet |
| PUT | /api/v1/wallets/freeze/{walletId} | Freeze wallet |
| PUT | /api/v1/wallets/activate/{walletId} | Activate wallet |


## Author

Azaz Khan

Java Backend Developer
