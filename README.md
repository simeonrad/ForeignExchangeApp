# Foreign Exchange Application

## Introduction
This application provides a simple foreign exchange service with endpoints for current exchange rates, currency conversion, and accessing conversion history. It's designed to be a self-contained, easy-to-run service for financial applications.

## Functional Requirements

### Exchange Rate Endpoint
- **Input**: A pair of currency codes (e.g., USD to EUR).
- **Output**: The current exchange rate between the two currencies.

### Currency Conversion Endpoint
- **Input**: An amount in the source currency, source currency code, and target currency code.
- **Output**: The converted amount in the target currency and a unique transaction identifier.

### Conversion History Endpoint
- **Input**: A transaction identifier or a transaction date for filtering purposes (at least one must be provided).
- **Output**: A paginated list of currency conversions filtered by the provided criteria.

### External Exchange Rate Integration
- The application utilizes an external service provider for fetching exchange rates.

### Error Handling
- Errors are handled gracefully, providing meaningful error messages and specific error codes.

## Technical Requirements

- **Self-Contained Application**: No additional setup or configuration is needed to run the application.
- **RESTful API Design**: Service endpoints are developed following REST principles using Spring Boot.
- **Build & Dependency Management**: The project uses Gradle for building and managing dependencies.
- **Use of Design Patterns**: Design patterns are implemented to enhance code quality.
- **Code Structure**: Organized code reflecting a clear separation of concerns.
- **Unit Testing**: Unit tests are included using Mockito to ensure application reliability.
- **API Documentation**: The API is fully documented using Swagger.

## How to Run the Application

### Using Docker

The application is containerized using Docker for consistency across different environments.

## Running the Application Using Docker

To run the latest version of the application, use the following command:

```sh
docker run -p 8080:8080 simeonrad/foreign-exchange-app:v1.1