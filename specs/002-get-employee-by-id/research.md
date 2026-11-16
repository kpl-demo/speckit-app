# Phase 0 Research: Get Employee By ID (`002-get-employee-by-id`)

**Date**: 2026-11-16
**Feature**: [spec.md](spec.md)

## Decisions & Rationale

### 1. Reusing Existing Domain, Repository, and DTO Classes
- **Decision**: Reuse `Employee`, `EmployeeRepository`, and `EmployeeResponse` without changes or class duplication.
- **Rationale**: `EmployeeRepository` already extends `JpaRepository<Employee, Long>`, which provides `Optional<Employee> findById(Long id)` out of the box. `EmployeeResponse` already represents all six employee attributes (`id`, `firstName`, `lastName`, `email`, `department`, `salary`), and `EmployeeServiceImpl` already contains the `toEmployeeResponse(Employee)` mapper.
- **Alternatives considered**:
  - Creating a separate `EmployeeDetailResponse` DTO (rejected: identical fields; violates user constraint "Reuse the existing Employee entity, repository, service and DTO. Do not duplicate existing classes.").

### 2. Service Method Signature & Naming
- **Decision**: Add `EmployeeResponse getEmployeeById(Long id)` to `EmployeeService` and implement it in `EmployeeServiceImpl` using `employeeRepository.findById(id).map(this::toEmployeeResponse).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id))`.
- **Rationale**: Matches the existing naming style (`getAllEmployees()`), leverages the class-level `@Transactional(readOnly = true)` annotation already present on `EmployeeServiceImpl`, and keeps the controller free of business/lookup logic (Constitution Principle I).
- **Alternatives considered**:
  - Returning `Optional<EmployeeResponse>` from `EmployeeService` to `EmployeeController` (rejected: pushes domain not-found handling into the controller instead of centralizing exception handling via `@RestControllerAdvice` per Constitution Principle III).

### 3. Employee-Not-Found Exception Handling (`ResourceNotFoundException` + `GlobalExceptionHandler`)
- **Decision**: Create `com.example.employee.exception.ResourceNotFoundException` (extending `RuntimeException`) and register an `@ExceptionHandler(ResourceNotFoundException.class)` in the existing `GlobalExceptionHandler` that returns HTTP `404 Not Found` with the project's established JSON error structure (`status`, `error`, `message`). Also add `@ExceptionHandler(MethodArgumentTypeMismatchException.class)` in `GlobalExceptionHandler` to return HTTP `400 Bad Request` when `{id}` is non-numeric.
- **Rationale**: Directly satisfies the user instruction ("Handle employee-not-found using a custom ResourceNotFoundException or the project's existing exception handling mechanism") and Constitution Principle III (Input Validation & Consistent Exception Handling).
- **Alternatives considered**:
  - Throwing `ResponseStatusException(HttpStatus.NOT_FOUND, ...)` directly (rejected: produces Spring's default ProblemDetail/error structure rather than the uniform `status`/`error`/`message` JSON map established in `GlobalExceptionHandler`).

### 4. Controller Endpoint Mapping (`GET /api/employees/{id}`)
- **Decision**: Add `@GetMapping("/{id}") public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id)` to the existing `EmployeeController` (which is already mapped to `@RequestMapping("/api/employees")`), leaving `@GetMapping getAllEmployees()` untouched.
- **Rationale**: Preserves existing `GET /api/employees` behavior (`FR-005`) while exposing `GET /api/employees/{id}` with HTTP `200 OK` when the employee exists.
- **Alternatives considered**:
  - Creating a second controller class (rejected: violates user instruction "Add GET /api/employees/{id} to the existing EmployeeController. Do not duplicate existing classes.").

### 5. Unit Testing Approach (JUnit 5 + Mockito)
- **Decision**: Extend the existing `EmployeeServiceImplTest` and `EmployeeControllerTest` test classes with new `@Test` methods covering `getEmployeeById` (found `200 OK`, missing `404 Not Found`, and invalid path variable `400 Bad Request`) while keeping all existing `getAllEmployees` tests intact.
- **Rationale**: Verifies both the new feature and zero regression on `GET /api/employees` in a single test suite (`mvn test`).
- **Alternatives considered**:
  - Creating duplicate test classes (rejected: extending the existing test classes for `EmployeeServiceImpl` and `EmployeeController` keeps unit tests cohesive and avoids duplication).

