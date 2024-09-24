package com.groceryshop.logistics.employees;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {
    EmployeeRepository employeeRepository;
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public Employee registerEmployee(final EmployeeRegisterRequestDTO registerInfo) {
        final Employee employeeToRegister = new Employee(
                null,
                registerInfo.employmentShopId(),
                registerInfo.firstname(),
                registerInfo.lastname(),
                registerInfo.position(),
                registerInfo.accessCode(),
                registerInfo.accessLevel()
        );
        return employeeRepository.registerEmployee(employeeToRegister);
    }

    @Transactional
    public Employee loginEmployee(final EmployeeLoginRequestDTO loginInfo) {
        // Retrieve the user from the database
        final Employee storedEmployee = employeeRepository.findEmployeeByShopIdAndAccessCode(loginInfo.employmentShopId(), loginInfo.accessCode());

        // Check if user exists
        if (storedEmployee == null) {
            throw new RuntimeException("Wrong login details");
        }

        // Compare raw password with stored hashed password
        if (loginInfo.employmentShopId().equals(storedEmployee.employmentShopId()) && loginInfo.accessCode().equals(storedEmployee.accessCode())) {
            return storedEmployee;
        } else {
            throw new RuntimeException("Wrong login details");
        }
    }
}
