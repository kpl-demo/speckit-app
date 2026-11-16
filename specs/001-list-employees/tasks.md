---

description: "Task list for Retrieve All Employees feature implementation"
---

# Tasks: Retrieve All Employees

**Input**: Design documents from `/specs/001-list-employees/`

**Prerequisites**: [plan.md](plan.md) (required), [spec.md](spec.md) (required for user stories), [research.md](research.md), [data-model.md](data-model.md), [contracts/employees-api.md](contracts/employees-api.md)

**Tests**: Included as required by the project constitution (Principle IV: Unit Testing with JUnit 5) and implementation plan (JUnit 5 + Mockito).

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2)
- Include exact file paths in descriptions

## Path Conventions

- **Single Maven project**: `pom.xml`, `src/main/java/com/example/employee/`, `src/main/resources/`, and `src/test/java/com/example/employee/` at repository root

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Maven project initialization, Java 17 / Spring Boot configuration, and package structure

- [X] T001 Create Maven project configuration with Java 17, Spring Boot 3.3.x parent, `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `spring-boot-starter-validation`, `mysql-connector-j`, and `spring-boot-starter-test` in `pom.xml`
- [X] T002 [P] Create Spring Boot main application class `EmployeeApplication` in `src/main/java/com/example/employee/EmployeeApplication.java`
- [X] T003 [P] Configure MySQL datasource URL, credentials, Hibernate DDL settings, and server port in `src/main/resources/application.properties`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core persistence model, DTO contract, repository interface, and centralized error handling that MUST be complete before user story implementation

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T004 [P] Create `Employee` JPA entity mapped to `@Table(name = "employees")` with fields `id` (`@Id`, `@GeneratedValue(strategy = GenerationType.IDENTITY)`), `firstName` (`@Column(name = "first_name", nullable = false, length = 100)`), `lastName` (`@Column(name = "last_name", nullable = false, length = 100)`), `email` (`@Column(name = "email", nullable = false, unique = true, length = 150)`), `department` (`@Column(name = "department", nullable = false, length = 100)`), and `salary` (`@Column(name = "salary", nullable = false, precision = 15, scale = 2)`) in `src/main/java/com/example/employee/entity/Employee.java`
- [X] T005 [P] Create `EmployeeResponse` DTO containing `Long id`, `String firstName`, `String lastName`, `String email`, `String department`, and `BigDecimal salary` in `src/main/java/com/example/employee/dto/EmployeeResponse.java`
- [X] T006 Create `EmployeeRepository` interface extending `JpaRepository<Employee, Long>` in `src/main/java/com/example/employee/repository/EmployeeRepository.java` (depends on T004)
- [X] T007 [P] Create `@RestControllerAdvice` class `GlobalExceptionHandler` handling `HttpRequestMethodNotSupportedException` (`405 Method Not Allowed`) and unexpected `Exception` (`500 Internal Server Error`) with consistent JSON error responses in `src/main/java/com/example/employee/exception/GlobalExceptionHandler.java`

**Checkpoint**: Foundation ready (`Employee`, `EmployeeResponse`, `EmployeeRepository`, `GlobalExceptionHandler`) - user story implementation can now begin

---

## Phase 3: User Story 1 - Retrieve Full Employee Directory (Priority: P1) 🎯 MVP

**Goal**: Expose `GET /api/employees` to return a JSON array of all employees with `id`, `firstName`, `lastName`, `email`, `department`, and `salary` and HTTP status `200 OK`

**Independent Test**: Run `mvn test` or invoke `GET /api/employees` when one or more employee records exist and verify HTTP `200 OK` with a JSON array of `EmployeeResponse` objects containing all six attributes

### Tests for User Story 1 ⚠️

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [X] T008 [P] [US1] Create JUnit 5 + Mockito unit test `EmployeeServiceImplTest` verifying `getAllEmployees()` retrieves single and multiple `Employee` entities from `EmployeeRepository` and maps all six fields to `EmployeeResponse` DTOs in `src/test/java/com/example/employee/service/EmployeeServiceImplTest.java`
- [X] T009 [P] [US1] Create `@WebMvcTest` unit test `EmployeeControllerTest` verifying `GET /api/employees` returns HTTP `200 OK`, `Content-Type: application/json`, and the expected JSON array of employees in `src/test/java/com/example/employee/controller/EmployeeControllerTest.java`

### Implementation for User Story 1

- [X] T010 [US1] Create `EmployeeService` interface declaring `List<EmployeeResponse> getAllEmployees()` in `src/main/java/com/example/employee/service/EmployeeService.java`
- [X] T011 [US1] Implement `EmployeeServiceImpl` with constructor-injected `EmployeeRepository`, `@Transactional(readOnly = true)`, and entity-to-`EmployeeResponse` mapping in `src/main/java/com/example/employee/service/EmployeeServiceImpl.java` (depends on T004, T005, T006, T010)
- [X] T012 [US1] Implement `EmployeeController` with `@RestController`, `@RequestMapping("/api/employees")`, constructor-injected `EmployeeService`, and `@GetMapping` returning `ResponseEntity<List<EmployeeResponse>>` with HTTP `200 OK` in `src/main/java/com/example/employee/controller/EmployeeController.java` (depends on T010, T011)

**Checkpoint**: At this point, User Story 1 (`GET /api/employees` for populated employee records) is fully functional and testable independently

---

## Phase 4: User Story 2 - Retrieve Employees When Directory Is Empty (Priority: P2)

**Goal**: Ensure `GET /api/employees` returns HTTP `200 OK` and an empty JSON array `[]` when no employee records exist

**Independent Test**: Run `mvn test` or invoke `GET /api/employees` against an empty employee repository/table and verify HTTP `200 OK` with an empty JSON array `[]`

### Tests for User Story 2 ⚠️

- [X] T013 [P] [US2] Add unit test in `EmployeeServiceImplTest` verifying `getAllEmployees()` returns an empty `List<EmployeeResponse>` when `EmployeeRepository.findAll()` returns an empty list in `src/test/java/com/example/employee/service/EmployeeServiceImplTest.java`
- [X] T014 [P] [US2] Add unit test in `EmployeeControllerTest` verifying `GET /api/employees` returns HTTP `200 OK` and an empty JSON array `[]` when `EmployeeService.getAllEmployees()` returns an empty list in `src/test/java/com/example/employee/controller/EmployeeControllerTest.java`

### Implementation for User Story 2

- [X] T015 [US2] Verify and refine `EmployeeServiceImpl.getAllEmployees()` to return a non-null empty `List<EmployeeResponse>` when no `Employee` entities exist in `src/main/java/com/example/employee/service/EmployeeServiceImpl.java`
- [X] T016 [US2] Verify and refine `EmployeeController.getAllEmployees()` to return HTTP `200 OK` with body `[]` and `Content-Type: application/json` when the employee list is empty in `src/main/java/com/example/employee/controller/EmployeeController.java`

**Checkpoint**: At this point, User Stories 1 AND 2 both work independently and pass unit tests

---

## Phase 5: Polish & Cross-Cutting Concerns

**Purpose**: Edge-case test coverage (unsupported write methods, internal error handling) and full build/quickstart validation

- [X] T017 [P] Add `@WebMvcTest` unit tests verifying `POST /api/employees`, `PUT /api/employees`, and `DELETE /api/employees` return `405 Method Not Allowed` and unexpected service exceptions return `500 Internal Server Error` without stack traces in `src/test/java/com/example/employee/controller/EmployeeControllerTest.java`
- [X] T018 Execute `mvn clean test` from repository root to validate Java 17 compilation, layered architecture compliance, and 100% passing JUnit 5 + Mockito unit tests per `specs/001-list-employees/quickstart.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately (`T001` → `T002`, `T003`)
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories (`T004`, `T005`, `T007` in parallel → `T006`)
- **User Story 1 (Phase 3 - P1 MVP)**: Depends on Foundational phase completion (`T008`, `T009` in parallel → `T010` → `T011` → `T012`)
- **User Story 2 (Phase 4 - P2)**: Depends on Foundational phase completion and integrates with `EmployeeServiceImpl` / `EmployeeController` from US1 (`T013`, `T014` in parallel → `T015` → `T016`)
- **Polish (Phase 5)**: Depends on User Stories 1 and 2 completion (`T017` → `T018`)

### User Story Dependencies

- **User Story 1 (P1)**: Starts immediately after Phase 2 (Foundational); zero dependency on other user stories.
- **User Story 2 (P2)**: Builds on the `GET /api/employees` endpoint and service created in US1 to guarantee empty-collection behavior (`[]` with `200 OK`) and dedicated unit test verification.

### Within Each User Story

- Unit tests (`T008`/`T009`, `T013`/`T014`) MUST be written first
- Service contract (`EmployeeService`) before service implementation (`EmployeeServiceImpl`)
- Service implementation (`EmployeeServiceImpl`) before controller endpoint (`EmployeeController`)

### Parallel Opportunities

- **Phase 1**: `T002` (`EmployeeApplication.java`) and `T003` (`application.properties`) can run in parallel after `T001` (`pom.xml`).
- **Phase 2**: `T004` (`Employee.java`), `T005` (`EmployeeResponse.java`), and `T007` (`GlobalExceptionHandler.java`) can all run in parallel across distinct files.
- **Phase 3 (US1)**: `T008` (`EmployeeServiceImplTest.java`) and `T009` (`EmployeeControllerTest.java`) can run in parallel.
- **Phase 4 (US2)**: `T013` (`EmployeeServiceImplTest.java`) and `T014` (`EmployeeControllerTest.java`) can run in parallel.

---

## Parallel Example: User Story 1

```bash
# Launch Foundational models & handlers in parallel (Phase 2):
Task T004: "Create Employee JPA entity in src/main/java/com/example/employee/entity/Employee.java"
Task T005: "Create EmployeeResponse DTO in src/main/java/com/example/employee/dto/EmployeeResponse.java"
Task T007: "Create GlobalExceptionHandler in src/main/java/com/example/employee/exception/GlobalExceptionHandler.java"

# Launch User Story 1 unit tests in parallel (Phase 3):
Task T008: "Create EmployeeServiceImplTest in src/test/java/com/example/employee/service/EmployeeServiceImplTest.java"
Task T009: "Create EmployeeControllerTest in src/test/java/com/example/employee/controller/EmployeeControllerTest.java"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup (`T001`–`T003`)
2. Complete Phase 2: Foundational (`T004`–`T007`)
3. Complete Phase 3: User Story 1 (`T008`–`T012`)
4. **STOP and VALIDATE**: Run `mvn test` to verify `GET /api/employees` retrieves and maps employee records accurately

### Incremental Delivery

1. Setup + Foundational → Core persistence, DTO, and exception handling ready
2. User Story 1 (P1) → Populated employee list endpoint + unit tests (MVP!)
3. User Story 2 (P2) → Empty employee directory (`[]`) verification + unit tests
4. Polish → Edge-case method/error tests + full `mvn clean test` validation
