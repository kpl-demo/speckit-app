package com.example.employee.controller;

import com.example.employee.dto.EmployeeResponse;
import com.example.employee.exception.GlobalExceptionHandler;
import com.example.employee.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@Import(GlobalExceptionHandler.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Test
    @DisplayName("GET /api/employees returns HTTP 200 and JSON array when employees exist")
    void getAllEmployees_whenEmployeesExist_returns200AndJsonArray() throws Exception {
        List<EmployeeResponse> employees = List.of(
                new EmployeeResponse(
                        1L,
                        "Alice",
                        "Smith",
                        "alice.smith@example.com",
                        "Engineering",
                        new BigDecimal("95000.00")
                ),
                new EmployeeResponse(
                        2L,
                        "Bob",
                        "Jones",
                        "bob.jones@example.com",
                        "Finance",
                        new BigDecimal("82500.50")
                )
        );
        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/api/employees").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Alice"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"))
                .andExpect(jsonPath("$[0].email").value("alice.smith@example.com"))
                .andExpect(jsonPath("$[0].department").value("Engineering"))
                .andExpect(jsonPath("$[0].salary").value(95000.00))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].firstName").value("Bob"))
                .andExpect(jsonPath("$[1].lastName").value("Jones"))
                .andExpect(jsonPath("$[1].email").value("bob.jones@example.com"))
                .andExpect(jsonPath("$[1].department").value("Finance"))
                .andExpect(jsonPath("$[1].salary").value(82500.50));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    @DisplayName("GET /api/employees returns HTTP 200 and empty JSON array when no employees exist")
    void getAllEmployees_whenNoEmployeesExist_returns200AndEmptyArray() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/employees").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    @DisplayName("Write operations (POST, PUT, DELETE) on /api/employees return HTTP 405 Method Not Allowed")
    void writeOperations_onEmployeesEndpoint_return405MethodNotAllowed() throws Exception {
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value(405));

        mockMvc.perform(put("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value(405));

        mockMvc.perform(delete("/api/employees"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value(405));
    }

    @Test
    @DisplayName("GET /api/employees returns HTTP 500 with structured JSON when unexpected error occurs")
    void getAllEmployees_whenUnexpectedExceptionThrown_returns500JsonError() throws Exception {
        when(employeeService.getAllEmployees()).thenThrow(new RuntimeException("Database connection lost"));

        mockMvc.perform(get("/api/employees").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }
}

