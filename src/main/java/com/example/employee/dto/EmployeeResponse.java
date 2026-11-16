package com.example.employee.dto;

import java.math.BigDecimal;

public record EmployeeResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String department,
        BigDecimal salary
) {
}

