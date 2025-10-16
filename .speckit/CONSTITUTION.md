# Drug Interactions API Technical Constitution

## Core Principles

### 1. Architecture and Design

#### SOLID Principles
- **Single Responsibility**: Each class has one reason to change
- **Open/Closed**: Open for extension, closed for modification
- **Liskov Substitution**: Subtypes must be substitutable for their base types
- **Interface Segregation**: Clients shouldn't depend on methods they don't use
- **Dependency Inversion**: Depend on abstractions, not concrete implementations

#### Hexagonal Architecture (Ports & Adapters)
- Separate core domain logic from external concerns
- Structure:
  - Domain (core business logic)
  - Application (use cases/services)
  - Infrastructure (adapters for external systems)
  - Interfaces (ports/contracts)
- Use dependency injection for flexible component coupling

### 2. Technical Standards

#### HTTP Client Usage
- Use WebClient for all HTTP communications
- Avoid legacy RestTemplate
- Configure timeout and retry policies
- Implement circuit breakers for external services

#### Data Management
- Use UUIDs as primary identifiers
- Implement proper database indexing strategies
- Follow database normalization principles
- Use migrations for schema changes

#### Validation
- Implement Bean Validation (JSR 380)
- Validate at domain boundaries
- Custom validators for complex business rules
- Clear validation error messages

### 3. Testing Standards

#### Unit Testing
- Use Mockito for mocking
- AssertJ for fluent assertions
- Test naming pattern: given_when_then
- Mock external dependencies
- Coverage requirements:
  - 80%+ line coverage for service layer
  - 80%+ branch coverage for service layer
  - 100% coverage for critical business logic

#### Integration Testing
- WireMock for external service simulation
- Test real database interactions
- Containerized testing environment
- Test API contracts

#### Test Quality
- Arrange-Act-Assert pattern
- One assertion per test
- Use test data builders
- Meaningful test names

### 4. Security

#### Basic Security Requirements
- HTTPS only in production
- JWT-based authentication
- Role-based access control
- Input validation against XSS
- OWASP Top 10 compliance
- Regular dependency security updates
- Secrets management using vault

### 5. Build and Deploy

#### Reproducible Builds
- Explicit dependency versions
- Lock files for dependencies
- Containerized builds
- Version control for configuration

#### Code Style and Quality
- Spotless for code formatting
- Checkstyle for style enforcement
- SonarQube for quality gates
- No warnings policy
- Maximum method length: 20 lines
- Maximum class length: 300 lines

### 6. API Documentation

#### Documentation Standards
- OpenAPI/Swagger documentation
- Clear endpoint descriptions
- Request/response examples
- Error scenarios documented
- Accessibility considerations:
  - Clear error messages
  - HTTP status codes
  - Response format consistency
  - Rate limiting headers
  - Language tags support

### 7. Monitoring and Observability

#### Observability Requirements
- Structured logging
- Distributed tracing
- Metrics collection
- Health check endpoints
- Performance monitoring

### 8. Version Control

#### Git Practices
- Feature branching
- Conventional commits
- Pull request reviews
- No direct commits to main
- Squash merging

### 9. Definition of Done

A feature is considered complete when:
1. Code follows SOLID principles
2. Unit tests achieve coverage targets
3. Integration tests verify functionality
4. Security review completed
5. Documentation updated
6. Code review approved
7. Style checks pass
8. Build succeeds
9. API accessibility verified

This constitution is a living document and should be reviewed and updated quarterly to reflect team learnings and evolving best practices.
