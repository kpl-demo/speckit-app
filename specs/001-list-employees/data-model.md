# Phase 1 Data Model: Retrieve All Employees (`001-list-employees`)

**Date**: 2026-11-16
**Feature**: [spec.md](spec.md)

## 1. Persistence Entity: `Employee`

- **Class**: `com.example.employee.entity.Employee`
- **Database Table**: `employees`
- **Description**: JPA entity representing a staff member stored in MySQL.

### Fields & Column Mapping

| Field Name | Java Type | Column Name | Constraints / JPA Annotations | Description |
|------------|-----------|-------------|-------------------------------|-------------|
| `id` | `Long` | `id` | `@Id`, `@GeneratedValue(strategy = GenerationType.IDENTITY)` | Primary key, unique employee identifier |
| `firstName` | `String` | `first_name` | `@Column(name = "first_name", nullable = false, length = 100)` | Employee's given name |
| `lastName` | `String` | `last_name` | `@Column(name = "last_name", nullable = false, length = 100)` | Employee's family name / surname |
| `email` | `String` | `email` | `@Column(name = "email", nullable = false, unique = true, length = 150)` | Employee's unique work email address |
| `department` | `String` | `department` | `@Column(name = "department", nullable = false, length = 100)` | Organizational department name |
| `salary` | `BigDecimal` | `salary` | `@Column(name = "salary", nullable = false, precision = 15, scale = 2)` | Employee's compensation amount |

### Entity Rules
- Must provide a protected/public no-argument constructor as required by JPA/Hibernate.
- Must provide an all-arguments constructor (or constructor without `id` + full constructor) and standard getters/setters for persistence and testing.
- Must **never** be serialized directly in HTTP responses (Constitution Principle II).

---

## 2. API Response DTO: `EmployeeResponse`

- **Class/Record**: `com.example.employee.dto.EmployeeResponse`
- **Description**: Immutable Data Transfer Object returned by `EmployeeService` and serialized as JSON by `EmployeeController`.

### Fields

| JSON Property | Java Type | Source Entity Field | Example Value |
|---------------|-----------|---------------------|---------------|
| `id` | `Long` | `Employee.id` | `1` |
| `firstName` | `String` | `Employee.firstName` | `"Alice"` |
| `lastName` | `String` | `Employee.lastName` | `"Smith"` |
| `email` | `String` | `Employee.email` | `"alice.smith@example.com"` |
| `department` | `String` | `Employee.department` | `"Engineering"` |
| `salary` | `BigDecimal` | `Employee.salary` | `95000.00` |

---

## 3. Repository Interface: `EmployeeRepository`

- **Interface**: `com.example.employee.repository.EmployeeRepository`
- **Superinterface**: `org.springframework.data.jpa.repository.JpaRepository<Employee, Long>`
- **Operations Used**:
  - `List<Employee> findAll()` — inherited from `JpaRepository`; returns all rows from `employees` or an empty `List` when no rows exist.

---

## 4. State Transitions

This feature is strictly **read-only** (`GET /api/employees`). No state transitions (`CREATE`, `UPDATE`, `DELETE`) are performed by the application in this feature scope.

