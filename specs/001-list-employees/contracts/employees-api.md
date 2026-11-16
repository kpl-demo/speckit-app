# API Contract: Retrieve All Employees (`001-list-employees`)

**Base Path**: `/api/employees`
**Media Type**: `application/json`

---

## 1. Retrieve All Employees

- **Endpoint**: `GET /api/employees`
- **Description**: Returns a JSON array of all employees in the system. Returns an empty array `[]` when no employees exist.
- **Request Parameters**: None
- **Request Body**: None

### Success Response — Employees Exist (`200 OK`)

**Status Code**: `200 OK`
**Headers**: `Content-Type: application/json`

```json
[
  {
    "id": 1,
    "firstName": "Alice",
    "lastName": "Smith",
    "email": "alice.smith@example.com",
    "department": "Engineering",
    "salary": 95000.00
  },
  {
    "id": 2,
    "firstName": "Bob",
    "lastName": "Jones",
    "email": "bob.jones@example.com",
    "department": "Finance",
    "salary": 82500.50
  }
]
```

### Success Response — No Employees Exist (`200 OK`)

**Status Code**: `200 OK`
**Headers**: `Content-Type: application/json`

```json
[]
```

---

## 2. Error Responses

### Unsupported HTTP Method (`405 Method Not Allowed`)

Returned when a client sends `POST`, `PUT`, `PATCH`, or `DELETE` to `/api/employees`.

**Status Code**: `405 Method Not Allowed`
**Headers**: `Content-Type: application/json`

```json
{
  "status": 405,
  "error": "Method Not Allowed",
  "message": "Request method 'POST' is not supported"
}
```

### Internal Server Error (`500 Internal Server Error`)

Returned when an unexpected runtime exception occurs during retrieval.

**Status Code**: `500 Internal Server Error`
**Headers**: `Content-Type: application/json`

```json
{
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred"
}
```

