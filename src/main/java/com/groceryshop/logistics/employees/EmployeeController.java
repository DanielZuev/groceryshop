package com.groceryshop.logistics.employees;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/groceryshop/employees")
public class EmployeeController {

    EmployeeService employeeService;
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/register")
    public ResponseEntity<Employee> registerEmployee(
            @RequestBody EmployeeRegisterRequestDTO registerInfo
    ) {
        Employee employee = employeeService.registerEmployee(registerInfo);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @PostMapping("/login/")
    public ResponseEntity<Employee> loginEmployee(
            @RequestBody EmployeeLoginRequestDTO loginInfo
    ) {
        Employee employee = employeeService.loginEmployee(loginInfo);
        return new ResponseEntity<>(employee, HttpStatus.ACCEPTED);
    }
}
