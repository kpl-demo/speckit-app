# API Contract: Get Employee By ID (`002-get-employee-by-id`)

**Base Path**: `/api/employees`
**Media Type**: `application/json`

---

## 1. Retrieve Employee by ID

- **Endpoint**: `GET /api/employees/{id}`
- **Description**: Retrieves a single employee matching the given `{id}` path variable.
- **Path Variables**:
  - `id` (`Long`, required): Unique numeric identifier of the employee.
- **Request Body**: None

### Success Response — Employee Exists (`200 OK`)

**Status Code**: `200 OK`
**Headers**: `Content-Type: application/json`

```json
{
  "id": 1,
  "firstName": "Alice",
  "lastName": "Smith",
  "email": "alice.smith@example.com",
  "department": "Engineering",
  "salary": 95000.00
}
```

---

## 2. Error Responses

### Employee Not Found (`404 Not Found`)

Returned when no employee record exists for the requested `{id}`.

**Status Code**: `404 Not Found`
**Headers**: `Content-Type: application/json`

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with id: 999"
}
```

### Invalid Path Variable Type (`400 Bad Request`)

Returned when `{id}` cannot be parsed as a valid `Long` (e.g., `GET /api/employees/abc`).

**Status Code**: `400 Bad Request`
**Headers**: `Content-Type: application/json`

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid parameter 'id': abc"
}
```

### Unsupported HTTP Method (`405 Method Not Allowed`)

Returned when a client sends `POST`, `PUT`, `PATCH`, or `DELETE` to `/api/employees/{id}`.

**Status Code**: `405 Method Not Allowed`
**Headers**: `Content-Type: application/json`

```json
{
  "status": 405,
  "error": "Method Not Allowed",
  "message": "Request method 'DELETE' is not supported"
}
```

