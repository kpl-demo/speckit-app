# Phase 0 Research: Retrieve All Employees (`001-list-employees`)

**Date**: 2026-11-16
**Feature**: [spec.md](spec.md)

## Decisions & Rationale

### 1. Spring Boot & Java Baseline
- **Decision**: Use **Java 17** with **Spring Boot 3.3.x** managed via **Apache Maven**.
- **Rationale**: Mandated by the project constitution and user input. Spring Boot 3.x natively targets Java 17+, Jakarta EE 10 (`jakarta.persistence.*`, `jakarta.validation.*`), and bundles JUnit 5 and Mockito in `spring-boot-starter-test`.
- **Alternatives considered**:
  - Spring Boot 2.7.x (rejected: EOL and uses legacy `javax.*` namespaces).
  - Gradle (rejected: constitution and user input require Maven).

### 2. Service Layer Contract (`EmployeeService` + `EmployeeServiceImpl`)
- **Decision**: Define an `EmployeeService` interface (`List<EmployeeResponse> getAllEmployees()`) and a concrete `@Service` implementation `EmployeeServiceImpl` annotated with `@Transactional(readOnly = true)` using constructor-based dependency injection for `EmployeeRepository`.
- **Rationale**: Directly requested by the architectural specification (`EmployeeService` + `EmployeeServiceImpl`) while adhering to Constitution Principle I (Strict Layered Architecture) and Principle V (constructor injection). Marking retrieval as `readOnly = true` optimizes Hibernate dirty-checking and read transaction handling.
- **Alternatives considered**:
  - Concrete service class without interface (rejected: user explicitly requested both `EmployeeService` and `EmployeeServiceImpl`).

### 3. DTO Representation & Entity-to-DTO Mapping
- **Decision**: Implement `EmployeeResponse` as a Java 17 `record` (or immutable DTO with standard getters) containing `Long id`, `String firstName`, `String lastName`, `String email`, `String department`, and `BigDecimal salary`, with a simple static factory or helper mapper inside `EmployeeServiceImpl`—without third-party mapping libraries.
- **Rationale**: Satisfies Constitution Principle II (DTO Boundary) and Principle V (Simplicity / YAGNI). Using a built-in Java 17 `record` and explicit mapping avoids external annotation processors like MapStruct or Lombok, keeping the Maven build fast and minimal.
- **Alternatives considered**:
  - MapStruct or ModelMapper (rejected: violates user instruction to "Avoid unnecessary abstractions and additional dependencies").
  - Returning `Employee` JPA entity directly (rejected: violates Constitution Principle II and user requirement for `EmployeeResponse` DTO).

### 4. Monetary Representation for `Salary`
- **Decision**: Use `java.math.BigDecimal` for the `salary` field in both `Employee` (`@Column( nullable = false, precision = 15, scale = 2 )`) and `EmployeeResponse`.
- **Rationale**: Financial/compensation values require exact decimal representation to prevent binary floating-point rounding errors inherent to `double`/`Double`.
- **Alternatives considered**:
  - `Double` (rejected: IEEE 754 floating-point inaccuracies on decimal currency values).

### 5. Consistent Exception Handling & Input/Method Validation
- **Decision**: Provide a minimal `@RestControllerAdvice` (`GlobalExceptionHandler`) that handles `HttpRequestMethodNotSupportedException` (returning HTTP `405 Method Not Allowed`) and unexpected `Exception` (returning HTTP `500 Internal Server Error` with a clean JSON error structure and no stack trace leakage).
- **Rationale**: Aligns with Constitution Principle III (Input Validation & Consistent Exception Handling) and edge cases in `spec.md` (unsupported write operations and unexpected internal errors) without adding external dependencies.
- **Alternatives considered**:
  - Relying solely on Spring Boot's default Whitelabel Error Page (rejected: can vary by `Accept` header and does not guarantee a uniform API error contract across REST handlers).

### 6. Unit Testing Strategy (JUnit 5 + Mockito)
- **Decision**:
  - **Service Unit Tests (`EmployeeServiceImplTest`)**: Use JUnit 5 (`@ExtendWith(MockitoExtension.class)`) with `@Mock EmployeeRepository` to test `getAllEmployees()` when multiple employees exist and when the repository returns an empty list.
  - **Controller Unit Tests (`EmployeeControllerTest`)**: Use `@WebMvcTest(EmployeeController.class)` with `@MockBean EmployeeService` and `MockMvc` to verify `GET /api/employees` returns HTTP `200 OK` with the expected JSON array when populated, HTTP `200 OK` with `[]` when empty, and rejects write methods (`POST`, `PUT`, `DELETE`).
- **Rationale**: Tests execute entirely in-memory in milliseconds without requiring a live MySQL server during `mvn test`, fulfilling Constitution Principle IV and user testing requirements.
- **Alternatives considered**:
  - Full `@SpringBootTest` requiring a live MySQL instance or Testcontainers (rejected: adds external Docker/DB runtime requirements and extra dependencies for unit testing).

