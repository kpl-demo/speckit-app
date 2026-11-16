package com.example.employee.service;

import com.example.employee.dto.EmployeeResponse;

import java.util.List;

public interface EmployeeService {

    List<EmployeeResponse> getAllEmployees();
}

