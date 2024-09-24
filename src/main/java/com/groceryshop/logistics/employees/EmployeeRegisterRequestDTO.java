package com.groceryshop.logistics.employees;

public record EmployeeRegisterRequestDTO(
        Integer employmentShopId,
        String firstname,
        String lastname,
        String position,
        Integer accessCode,
        EmployeeAccessLevel accessLevel
) {
}
