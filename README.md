# Drug Interactions API

A Spring Boot 3.3 application that manages drug interaction information and analyzes adverse event signals from OpenFDA.

## Project Structure

```
drug-interactions-api/
├── app/                    # Web layer (controllers, configuration)
├── domain/                 # Core business logic and ports
├── adapters/
│   ├── openfda/           # OpenFDA integration
│   └── memory/            # In-memory storage
└── tests/                 # Shared test fixtures
```

## Features

- Drug interaction management
- OpenFDA adverse event signal analysis
- Reactive WebClient integration
- In-memory storage
- Comprehensive test suite

## Prerequisites

- Java 17 or higher
- Maven 3.8+
- OpenFDA API key (optional)

## Configuration

### Application Properties

```yaml
openfda:
  base-url: https://api.fda.gov/drug/event.json
  api-key: ${FDA_API_KEY:}  # Optional
  timeout: 5000
  default-limit: 100
```

## API Endpoints

### Interactions

- `POST /interactions` - Create/update drug interaction
- `GET /interactions?drugA={drug}&drugB={drug}` - Get interaction details

### Signals

- `GET /signals?drugA={drug}&drugB={drug}&limit={limit}` - Get adverse event signals

## Building and Running

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run -pl app

# Run tests
mvn test
```

## Development

The project follows hexagonal architecture:
- Controllers -> Services (Domain) -> Ports
- Adapters implement the ports
- Clear separation between domain logic and external concerns

## Testing

The project includes:
- Unit tests for domain logic
- Integration tests with WireMock for OpenFDA
- Shared test fixtures for consistency
