# Quickstart & Validation Guide: Get Employee By ID (`002-get-employee-by-id`)

**Feature**: [spec.md](spec.md)
**Data Model**: [data-model.md](data-model.md)
**API Contract**: [contracts/employee-by-id-api.md](contracts/employee-by-id-api.md)

## 1. Prerequisites

- **JDK 17+** and **Apache Maven 3.8+**
- **MySQL** (only required for live runtime requests; unit tests run in-memory via JUnit 5 and Mockito)

## 2. Run Automated Unit Tests (No Database Required)

Execute all unit tests (both existing `GET /api/employees` tests and new `GET /api/employees/{id}` tests):

```powershell
mvn test
```

**Expected Outcome**:
- `EmployeeServiceImplTest` passes all tests (`getAllEmployees` populated/empty + `getEmployeeById` found/not-found).
- `EmployeeControllerTest` passes all tests (`GET /api/employees` + `GET /api/employees/{id}` `200 OK`, `404 Not Found`, `400 Bad Request`, `405 Method Not Allowed`, and `500 Internal Server Error`).
- Build reports `BUILD SUCCESS` with 0 failures.

## 3. Validate Live API Scenarios

Start the application:

```powershell
mvn spring-boot:run
```

### Scenario A: Retrieve an Existing Employee (`200 OK`)

Assuming an employee with `id = 1` exists in the `employees` table:

```powershell
curl -i http://localhost:8080/api/employees/1
```

**Expected Output**:
- HTTP status: `200 OK`
- Content-Type: `application/json`
- Body: Single JSON object matching [contracts/employee-by-id-api.md](contracts/employee-by-id-api.md).

### Scenario B: Retrieve a Non-Existent Employee (`404 Not Found`)

Query an ID that does not exist (e.g., `999`):

```powershell
curl -i http://localhost:8080/api/employees/999
```

**Expected Output**:
- HTTP status: `404 Not Found`
- Content-Type: `application/json`
- Body:
  ```json
  {
    "status": 404,
    "error": "Not Found",
    "message": "Employee not found with id: 999"
  }
  ```

### Scenario C: Existing List Endpoint Remains Unchanged (`200 OK`)

```powershell
curl -i http://localhost:8080/api/employees
```

**Expected Output**:
- HTTP status: `200 OK`
- Body: JSON array of all employees (or `[]` if empty).

