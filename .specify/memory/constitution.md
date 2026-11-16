<!--
Sync Impact Report
- Version change: N/A (template) → 1.0.0
- Modified principles:
  - [PRINCIPLE_1_NAME] → I. Strict Layered Architecture
  - [PRINCIPLE_2_NAME] → II. RESTful Design & DTO Boundary
  - [PRINCIPLE_3_NAME] → III. Input Validation & Consistent Exception Handling
  - [PRINCIPLE_4_NAME] → IV. Unit Testing with JUnit 5
  - [PRINCIPLE_5_NAME] → V. Clean Code, SOLID & Simplicity
- Added sections:
  - Technology Stack & Standards
  - Development Workflow & Quality Gates
- Removed sections: None
- Follow-up TODOs: None
-->
# Spring Boot REST API Constitution

## Core Principles

### I. Strict Layered Architecture
Every feature MUST follow a four-layer architecture: `Controller`, `Service`, `Repository`,
and `Entity`.
- **Controller layer**: Handles HTTP routing, request deserialization, input validation triggers,
  and response serialization only. Controllers MUST NOT contain business logic or access
  repositories directly.
- **Service layer**: Encapsulates all business rules, transaction boundaries (`@Transactional`),
  and mapping between entities and DTOs.
- **Repository layer**: Uses Spring Data JPA interfaces exclusively for database persistence operations.
- **Entity layer**: Models relational database tables via JPA annotations and MUST NOT leak across
  API boundaries.

**Rationale**: Enforcing strict separation of concerns prevents tight coupling between HTTP
transport, domain logic, and persistence, keeping every layer independently testable and easy
to maintain.

### II. RESTful Design & DTO Boundary
All external endpoints MUST adhere to RESTful conventions (resource-oriented plural nouns,
standard HTTP methods `GET`, `POST`, `PUT`, `PATCH`, `DELETE`, and accurate HTTP status codes).
- JPA `Entity` classes MUST NEVER be returned directly in API responses or accepted directly as
  request payloads.
- Dedicated Data Transfer Objects (DTOs) MUST be used for all API responses and request bodies.

**Rationale**: Decoupling the external API contract from internal persistence models prevents
accidental data exposure, lazy-loading serialization failures, and breaking API changes when the
database schema evolves.

### III. Input Validation & Consistent Exception Handling
All incoming API requests MUST be validated at the boundary, and all runtime failures MUST produce
a uniform error response structure.
- Request DTOs and path/query parameters MUST use Jakarta Bean Validation annotations (`@Valid`,
  `@NotNull`, `@NotBlank`, `@Size`, etc.).
- Exceptions MUST be handled centrally via `@RestControllerAdvice` and `@ExceptionHandler`
  components, translating domain, validation, and infrastructure errors into consistent JSON error
  payloads with appropriate HTTP status codes (`400`, `404`, `409`, `500`).
- Stack traces and internal implementation details MUST NEVER be exposed in client responses.

**Rationale**: Centralized validation and error handling guarantee predictable client behavior,
prevent security leaks, and eliminate repetitive try/catch blocks in controllers.

### IV. Unit Testing with JUnit 5
All business logic and API contracts MUST be verified through automated JUnit 5 unit tests.
- Service classes MUST have unit tests covering happy paths, edge cases, and exception scenarios,
  isolating dependencies with mocks where appropriate.
- Controller endpoints, request validation rules, and global exception handlers MUST be tested to
  verify HTTP status codes and response DTO structures.
- Every bug fix or new feature MUST include corresponding JUnit 5 test coverage before merge.

**Rationale**: Automated JUnit 5 tests provide fast feedback, document expected behavior, and
protect against regressions during refactoring.

### V. Clean Code, SOLID & Simplicity
All code MUST adhere to Clean Code practices, SOLID object-oriented design principles, and YAGNI
(You Aren't Gonna Need It).
- Classes and methods MUST have a single, well-defined responsibility with descriptive,
  intention-revealing names.
- Dependency injection MUST use constructor injection; field injection (`@Autowired` on fields)
  is prohibited.
- Avoid premature abstractions, speculative patterns, or unnecessary dependencies when a direct,
  standard Spring Boot solution suffices.

**Rationale**: Prioritizing simplicity and SOLID design keeps cognitive load low and ensures long-term
maintainability for a lightweight Spring Boot service.

## Technology Stack & Standards

The project MUST adhere to the following baseline technology choices:
- **Language**: Java 17 (using modern language features such as `record` types for DTOs where
  applicable)
- **Framework**: Spring Boot (Spring Web, Spring Validation, Spring Data JPA)
- **Build Tool**: Apache Maven (`pom.xml` as the single source of truth for dependencies and build
  lifecycle)
- **Database**: MySQL for relational persistence, accessed through Spring Data JPA / Hibernate
- **Testing**: JUnit 5 (Jupiter) with standard Spring Boot test utilities

Introducing new frameworks, alternative persistence engines, or build tools requires a formal
constitutional amendment.

## Development Workflow & Quality Gates

All changes MUST pass the following quality gates before completion:
1. **Build & Compile**: `mvn clean compile` MUST succeed with zero errors under Java 17.
2. **Automated Tests**: `mvn test` MUST execute all JUnit 5 tests with zero failures.
3. **Architecture & DTO Review**: Code reviews MUST verify that controllers delegate to services,
   services interact with repositories, JPA entities are never returned from controllers, and
   constructor injection is used throughout.
4. **Validation & Error Coverage**: Every new or modified endpoint MUST include input validation
   annotations and map failure cases through the global exception handler.

## Governance

This Constitution supersedes conflicting ad-hoc practices within the repository.
- **Amendment Procedure**: Any change to Core Principles, Technology Stack, or Governance MUST be
  proposed via an update to `.specify/memory/constitution.md`, accompanied by a clear rationale and
  migration plan for affected code.
- **Versioning Policy**: The constitution version follows Semantic Versioning (`MAJOR.MINOR.PATCH`):
  - `MAJOR`: Backward-incompatible removals or redefinitions of core principles or technology stack.
  - `MINOR`: Addition of new principles, sections, or materially expanded mandatory guidance.
  - `PATCH`: Wording clarifications, formatting refinements, or non-semantic fixes.
- **Compliance Review**: All specifications (`/speckit-specify`), implementation plans
  (`/speckit-plan`), task breakdowns (`/speckit-tasks`), and code reviews MUST verify alignment with
  these principles. Any architectural deviation MUST be explicitly justified and documented.

**Version**: 1.0.0 | **Ratified**: 2026-11-16 | **Last Amended**: 2026-11-16
