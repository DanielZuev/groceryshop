package com.groceryshop.logistics.employees;

public record EmployeeLoginRequestDTO(
        Integer employmentShopId,
        Integer accessCode
) {
}
