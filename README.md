/speckit-constitution

Create project principles for a simple Java Spring Boot REST API.

Technology standards:
- Java 17
- Spring Boot
- Maven
- Spring Data JPA
- MySQL
- RESTful API design
- Layered architecture: Controller, Service, Repository, Entity
- Use DTOs for API responses
- Follow clean code and SOLID principles
- Add JUnit 5 unit tests
- Validate inputs and handle exceptions consistently
- Keep the implementation simple and maintainable.




/speckit-specify

Create a simple Employee Management REST API.

The application should expose an endpoint to retrieve all employees.

Functional requirements:
1. Provide GET /api/employees.
2. Return a list of all employees.
3. Each employee should contain:
   - Employee ID
   - First name
   - Last name
   - Email
   - Department
   - Salary
4. Return HTTP 200 when employees are retrieved successfully.
5. Return an empty JSON array when no employees exist.
6. Use appropriate JSON response format.

Keep this feature limited to retrieving employees. Do not implement create, update, or delete operations.

Do not implement the code yet.





/speckit-plan

Implement this feature using:

- Java 17
- Spring Boot
- Maven
- Spring Web
- Spring Data JPA
- MySQL
- Hibernate

Architecture:
- Employee entity
- EmployeeRepository
- EmployeeService
- EmployeeServiceImpl
- EmployeeController
- EmployeeResponse DTO

Use constructor-based dependency injection.
Use standard REST conventions.
Add JUnit 5 and Mockito tests.

Keep the implementation minimal. Avoid unnecessary abstractions and additional dependencies.
