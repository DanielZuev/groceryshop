package com.groceryshop.logistics.employees;

public record Employee(
        Integer id,
        Integer employmentShopId,
        String firstname,
        String lastname,
        String position,
        Integer accessCode,
        EmployeeAccessLevel accessLevel
) {
}
