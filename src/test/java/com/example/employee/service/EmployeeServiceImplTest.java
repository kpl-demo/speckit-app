package com.example.employee.service;

import com.example.employee.dto.EmployeeResponse;
import com.example.employee.entity.Employee;
import com.example.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    @DisplayName("getAllEmployees returns mapped EmployeeResponse list when multiple employees exist")
    void getAllEmployees_whenEmployeesExist_returnsMappedDtoList() {
        Employee emp1 = new Employee(
                1L,
                "Alice",
                "Smith",
                "alice.smith@example.com",
                "Engineering",
                new BigDecimal("95000.00")
        );
        Employee emp2 = new Employee(
                2L,
                "Bob",
                "Jones",
                "bob.jones@example.com",
                "Finance",
                new BigDecimal("82500.50")
        );
        when(employeeRepository.findAll()).thenReturn(List.of(emp1, emp2));

        List<EmployeeResponse> result = employeeService.getAllEmployees();

        assertNotNull(result);
        assertEquals(2, result.size());

        EmployeeResponse first = result.get(0);
        assertEquals(1L, first.id());
        assertEquals("Alice", first.firstName());
        assertEquals("Smith", first.lastName());
        assertEquals("alice.smith@example.com", first.email());
        assertEquals("Engineering", first.department());
        assertEquals(new BigDecimal("95000.00"), first.salary());

        EmployeeResponse second = result.get(1);
        assertEquals(2L, second.id());
        assertEquals("Bob", second.firstName());
        assertEquals("Jones", second.lastName());
        assertEquals("bob.jones@example.com", second.email());
        assertEquals("Finance", second.department());
        assertEquals(new BigDecimal("82500.50"), second.salary());

        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getAllEmployees returns empty list when no employees exist")
    void getAllEmployees_whenNoEmployeesExist_returnsEmptyList() {
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        List<EmployeeResponse> result = employeeService.getAllEmployees();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(employeeRepository, times(1)).findAll();
    }
}

