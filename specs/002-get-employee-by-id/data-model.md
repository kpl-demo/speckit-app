# Phase 1 Data Model: Get Employee By ID (`002-get-employee-by-id`)

**Date**: 2026-11-16
**Feature**: [spec.md](spec.md)

## 1. Reused Persistence Entity: `Employee` (Unchanged)

- **Class**: `com.example.employee.entity.Employee`
- **Database Table**: `employees`
- **Status**: Existing class from `001-list-employees` — reused without modification.

| Field Name | Java Type | Column Name | Constraints / JPA Annotations |
|------------|-----------|-------------|-------------------------------|
| `id` | `Long` | `id` | `@Id`, `@GeneratedValue(strategy = GenerationType.IDENTITY)` |
| `firstName` | `String` | `first_name` | `@Column(name = "first_name", nullable = false, length = 100)` |
| `lastName` | `String` | `last_name` | `@Column(name = "last_name", nullable = false, length = 100)` |
| `email` | `String` | `email` | `@Column(name = "email", nullable = false, unique = true, length = 150)` |
| `department` | `String` | `department` | `@Column(name = "department", nullable = false, length = 100)` |
| `salary` | `BigDecimal` | `salary` | `@Column(name = "salary", nullable = false, precision = 15, scale = 2)` |

---

## 2. Reused API Response DTO: `EmployeeResponse` (Unchanged)

- **Record**: `com.example.employee.dto.EmployeeResponse`
- **Status**: Existing Java 17 `record` from `001-list-employees` — reused without modification.
- **Fields**: `Long id`, `String firstName`, `String lastName`, `String email`, `String department`, `BigDecimal salary`.

---

## 3. Reused Repository Interface: `EmployeeRepository` (Unchanged)

- **Interface**: `com.example.employee.repository.EmployeeRepository`
- **Superinterface**: `JpaRepository<Employee, Long>`
- **Operation Used by This Feature**:
  - `Optional<Employee> findById(Long id)` — inherited from `CrudRepository` / `JpaRepository`. Returns `Optional.of(employee)` when a record with primary key `id` exists, or `Optional.empty()` when no matching record exists.

---

## 4. Error Payload Model (Produced by `GlobalExceptionHandler`)

- **Structure**: `Map<String, Object>` serialized as JSON for `404 Not Found` and `400 Bad Request` responses (consistent with existing `405` and `500` handlers).

| JSON Property | Type | Description | Example (`404 Not Found`) |
|---------------|------|-------------|---------------------------|
| `status` | `Integer` | HTTP status code | `404` |
| `error` | `String` | Standard HTTP reason phrase | `"Not Found"` |
| `message` | `String` | Human-readable explanation | `"Employee not found with id: 999"` |

---

## 5. State Transitions

This feature is strictly **read-only** (`GET /api/employees/{id}`). No database mutations (`INSERT`, `UPDATE`, `DELETE`) occur.

