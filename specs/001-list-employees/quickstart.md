# Quickstart & Validation Guide: Retrieve All Employees (`001-list-employees`)

**Feature**: [spec.md](spec.md)
**Data Model**: [data-model.md](data-model.md)
**API Contract**: [contracts/employees-api.md](contracts/employees-api.md)

## 1. Prerequisites

- **JDK 17** installed and on `PATH` (`java -version`)
- **Apache Maven 3.8+** installed and on `PATH` (`mvn -version`)
- **MySQL 8.x** (required only for live runtime execution; unit tests run in-memory via JUnit 5 and Mockito without a database)

## 2. Run Unit Tests (No Database Required)

Verify all unit tests for `EmployeeServiceImpl` and `EmployeeController`:

```powershell
mvn clean test
```

**Expected Outcome**:
- `EmployeeServiceImplTest` passes (populated employee list mapping & empty list handling).
- `EmployeeControllerTest` passes (`GET /api/employees` returns `200 OK` with expected JSON array, `200 OK` with `[]` when empty, and `405 Method Not Allowed` for unsupported write methods).
- Build reports `BUILD SUCCESS` with 0 test failures.

## 3. Run the Application Locally with MySQL

1. Ensure a MySQL database named `employee_db` exists (or configure `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, and `SPRING_DATASOURCE_PASSWORD` in `src/main/resources/application.properties` or environment variables):
   ```sql
   CREATE DATABASE IF NOT EXISTS employee_db;
   ```

2. Start the Spring Boot application:
   ```powershell
   mvn spring-boot:run
   ```

## 4. Validate API Scenarios

### Scenario A: Empty Employee Directory (`200 OK`)

With an empty `employees` table, query the endpoint:

```powershell
curl -i http://localhost:8080/api/employees
```

**Expected Output**:
- HTTP status: `200 OK`
- Content-Type: `application/json`
- Body: `[]`

### Scenario B: Populated Employee Directory (`200 OK`)

Insert sample rows into `employees` in MySQL and repeat:

```powershell
curl -i http://localhost:8080/api/employees
```

**Expected Output**:
- HTTP status: `200 OK`
- Content-Type: `application/json`
- Body: JSON array of employee objects matching [contracts/employees-api.md](contracts/employees-api.md).

