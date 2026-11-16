---

description: "Task list for Get Employee By ID feature implementation"
---

# Tasks: Get Employee By ID

**Input**: Design documents from `/specs/002-get-employee-by-id/`

**Prerequisites**: [plan.md](plan.md) (required), [spec.md](spec.md) (required for user stories), [research.md](research.md), [data-model.md](data-model.md), [contracts/employee-by-id-api.md](contracts/employee-by-id-api.md)

**Tests**: Included as required by the project constitution (Principle IV: Unit Testing with JUnit 5) and implementation plan (JUnit 5 + Mockito).

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story while reusing existing components from `001-list-employees`.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2)
- Include exact file paths in descriptions

## Path Conventions

- **Single Maven project**: `src/main/java/com/example/employee/` and `src/test/java/com/example/employee/` at repository root

---

## Phase 1: Setup (Existing Baseline Verification)

**Purpose**: Confirm existing entity, repository, and DTO classes from `001-list-employees` are intact and reused without duplication

- [X] T001 Verify existing `Employee` entity (`id` with `@Id`, `@GeneratedValue(strategy = GenerationType.IDENTITY)`, `firstName` with `@Column(name = "first_name", nullable = false, length = 100)`, `lastName` with `@Column(name = "last_name", nullable = false, length = 100)`, `email` with `@Column(name = "email", nullable = false, unique = true, length = 150)`, `department` with `@Column(name = "department", nullable = false, length = 100)`, and `salary` with `@Column(name = "salary", nullable = false, precision = 15, scale = 2)`), `EmployeeRepository`, and `EmployeeResponse` DTO are reused unchanged in `src/main/java/com/example/employee/entity/Employee.java`, `src/main/java/com/example/employee/repository/EmployeeRepository.java`, and `src/main/java/com/example/employee/dto/EmployeeResponse.java`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Domain not-found exception and centralized HTTP `404` / `400` exception handling required by service and controller layers

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T002 Create custom `ResourceNotFoundException` extending `RuntimeException` with a message constructor in `src/main/java/com/example/employee/exception/ResourceNotFoundException.java`
- [X] T003 Update `GlobalExceptionHandler` to add `@ExceptionHandler(ResourceNotFoundException.class)` (returning HTTP `404 Not Found` with JSON keys `status`, `error`, `message`) and `@ExceptionHandler(MethodArgumentTypeMismatchException.class)` (returning HTTP `400 Bad Request` for non-numeric path variables) in `src/main/java/com/example/employee/exception/GlobalExceptionHandler.java` (depends on T002)

**Checkpoint**: Foundation ready (`ResourceNotFoundException` and `GlobalExceptionHandler` 404/400 handlers) - user story implementation can now begin

---

## Phase 3: User Story 1 - Retrieve Existing Employee Details by ID (Priority: P1) 🎯 MVP

**Goal**: Expose `GET /api/employees/{id}` to retrieve a single employee by ID using `EmployeeRepository.findById(id)` and return an `EmployeeResponse` JSON object with HTTP `200 OK`

**Independent Test**: Run `mvn test` or invoke `GET /api/employees/1` when employee `1` exists and verify HTTP `200 OK` with a JSON object containing `id`, `firstName`, `lastName`, `email`, `department`, and `salary`

### Tests for User Story 1 ⚠️

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [X] T004 [P] [US1] Add JUnit 5 + Mockito unit test `getEmployeeById_whenEmployeeExists_returnsMappedDto` in `src/test/java/com/example/employee/service/EmployeeServiceImplTest.java`
- [X] T005 [P] [US1] Add `@WebMvcTest` unit test `getEmployeeById_whenEmployeeExists_returns200AndJsonObject` verifying `GET /api/employees/{id}` returns HTTP `200 OK` and all six employee fields in `src/test/java/com/example/employee/controller/EmployeeControllerTest.java`

### Implementation for User Story 1

- [X] T006 [US1] Add `EmployeeResponse getEmployeeById(Long id)` method declaration to `EmployeeService` in `src/main/java/com/example/employee/service/EmployeeService.java`
- [X] T007 [US1] Implement `getEmployeeById(Long id)` in `EmployeeServiceImpl` using `employeeRepository.findById(id)` and `this::toEmployeeResponse` in `src/main/java/com/example/employee/service/EmployeeServiceImpl.java` (depends on T002, T006)
- [X] T008 [US1] Add `@GetMapping("/{id}")` method `getEmployeeById(@PathVariable Long id)` to `EmployeeController` returning `ResponseEntity.ok(employeeService.getEmployeeById(id))` without modifying `getAllEmployees()` in `src/main/java/com/example/employee/controller/EmployeeController.java` (depends on T006, T007)

**Checkpoint**: At this point, User Story 1 (`GET /api/employees/{id}` for existing employees) is fully functional and testable independently

---

## Phase 4: User Story 2 - Handle Non-Existent Employee Lookup (Priority: P2)

**Goal**: Return HTTP `404 Not Found` and a meaningful structured JSON error payload when `GET /api/employees/{id}` is called for a non-existent employee ID

**Independent Test**: Run `mvn test` or invoke `GET /api/employees/999` for a non-existent ID and verify HTTP `404 Not Found` with JSON body `{"status": 404, "error": "Not Found", "message": "Employee not found with id: 999"}`

### Tests for User Story 2 ⚠️

- [X] T009 [P] [US2] Add JUnit 5 + Mockito unit test `getEmployeeById_whenEmployeeDoesNotExist_throwsResourceNotFoundException` verifying `ResourceNotFoundException` is thrown with message `"Employee not found with id: 999"` in `src/test/java/com/example/employee/service/EmployeeServiceImplTest.java`
- [X] T010 [P] [US2] Add `@WebMvcTest` unit test `getEmployeeById_whenEmployeeDoesNotExist_returns404JsonError` verifying `GET /api/employees/999` returns HTTP `404 Not Found` and JSON fields `status`, `error`, and `message` in `src/test/java/com/example/employee/controller/EmployeeControllerTest.java`

### Implementation for User Story 2

- [X] T011 [US2] Ensure `EmployeeServiceImpl.getEmployeeById(Long id)` throws `new ResourceNotFoundException("Employee not found with id: " + id)` when `employeeRepository.findById(id)` is empty in `src/main/java/com/example/employee/service/EmployeeServiceImpl.java`
- [X] T012 [US2] Verify `GlobalExceptionHandler` returns HTTP `404 Not Found` with `status: 404`, `error: "Not Found"`, and `message: ex.getMessage()` when `ResourceNotFoundException` is thrown by `EmployeeController` in `src/main/java/com/example/employee/exception/GlobalExceptionHandler.java`

**Checkpoint**: At this point, User Stories 1 AND 2 work independently and existing `GET /api/employees` behavior remains untouched

---

## Phase 5: Polish & Cross-Cutting Concerns

**Purpose**: Edge-case test coverage (malformed path variable `400 Bad Request`) and full regression test execution

- [X] T013 [P] Add `@WebMvcTest` unit test `getEmployeeById_whenIdIsNonNumeric_returns400BadRequest` verifying `GET /api/employees/abc` returns HTTP `400 Bad Request` in `src/test/java/com/example/employee/controller/EmployeeControllerTest.java`
- [X] T014 Execute `mvn test` from repository root to verify all new and existing JUnit 5 + Mockito tests pass with zero regressions per `specs/002-get-employee-by-id/quickstart.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - verifies existing baseline (`T001`)
- **Foundational (Phase 2)**: Depends on Phase 1 (`T002` → `T003`) - BLOCKS user story implementation
- **User Story 1 (Phase 3 - P1 MVP)**: Depends on Foundational phase completion (`T004`, `T005` in parallel → `T006` → `T007` → `T008`)
- **User Story 2 (Phase 4 - P2)**: Depends on Foundational phase completion and integrates with `getEmployeeById` from US1 (`T009`, `T010` in parallel → `T011` → `T012`)
- **Polish (Phase 5)**: Depends on User Stories 1 and 2 completion (`T013` → `T014`)

### User Story Dependencies

- **User Story 1 (P1)**: Starts immediately after Phase 2 (Foundational).
- **User Story 2 (P2)**: Verifies and completes the `ResourceNotFoundException` → `404 Not Found` path on the `getEmployeeById` flow introduced in US1.

### Parallel Opportunities

- **Phase 3 (US1)**: `T004` (`EmployeeServiceImplTest.java`) and `T005` (`EmployeeControllerTest.java`) can run in parallel.
- **Phase 4 (US2)**: `T009` (`EmployeeServiceImplTest.java`) and `T010` (`EmployeeControllerTest.java`) can run in parallel.

---

## Parallel Example: User Story 1

```bash
# Launch User Story 1 unit tests in parallel (Phase 3):
Task T004: "Add getEmployeeById_whenEmployeeExists_returnsMappedDto in src/test/java/com/example/employee/service/EmployeeServiceImplTest.java"
Task T005: "Add getEmployeeById_whenEmployeeExists_returns200AndJsonObject in src/test/java/com/example/employee/controller/EmployeeControllerTest.java"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Baseline Verification (`T001`)
2. Complete Phase 2: Foundational (`T002`–`T003`)
3. Complete Phase 3: User Story 1 (`T004`–`T008`)
4. **STOP and VALIDATE**: Run `mvn test` to verify `GET /api/employees/{id}` returns `200 OK` with matching employee details

### Incremental Delivery

1. Setup + Foundational → `ResourceNotFoundException` & `GlobalExceptionHandler` ready
2. User Story 1 (P1) → `GET /api/employees/{id}` success path + unit tests (MVP!)
3. User Story 2 (P2) → `404 Not Found` missing employee handling + unit tests
4. Polish → Invalid path variable `400 Bad Request` test + full `mvn test` regression suite
