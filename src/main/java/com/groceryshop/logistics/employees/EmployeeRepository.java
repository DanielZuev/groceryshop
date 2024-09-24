package com.groceryshop.logistics.employees;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class EmployeeRepository extends NamedParameterJdbcDaoSupport {
    private final RowMapper<Employee> employeeRowMapper = (rs, rowNum) ->
            new Employee(
                rs.getInt("employee_id"),
                rs.getInt("employment_shop_id"),
                rs.getString("employee_firstname"),
                rs.getString("employee_lastname"),
                rs.getString("employee_position"),
                rs.getInt("employee_access_code"),
                EmployeeAccessLevel.valueOf(rs.getString("employee_access_level"))
            );

    public EmployeeRepository(final DataSource dataSource) {
        setDataSource(dataSource);
    }

    public Employee registerEmployee(final Employee employee) {
        final String sql = "INSERT INTO employees (" +
                "employment_shop_id, " +
                "employee_firstname, " +
                "employee_lastname" +
                "employee_position" +
                "employee_access_code" +
                "employee_access_level) " +
                "VALUES (" +
                ":employmentShopId, " +
                ":firstname" +
                ":lastname" +
                ":position" +
                ":accessCode" +
                ":accessLevel)";

        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("employmentShopId", employee.employmentShopId())
                .addValue("firstname", employee.firstname())
                .addValue("lastname", employee.lastname())
                .addValue("position", employee.position())
                .addValue("accessCode", employee.accessCode())
                .addValue("accessLevel", employee.accessLevel());
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            getNamedParameterJdbcTemplate().update(sql, params, keyHolder, new String[] { "employee_id" });
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("Employee with access code " + employee.accessCode() + " in shop with ID " + employee.employmentShopId() + " already exists");
        }
        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Failed to retrieve generated key");
        }

        return new Employee(
                key.intValue(),
                employee.employmentShopId(),
                employee.firstname(),
                employee.lastname(),
                employee.position(),
                employee.accessCode(),
                employee.accessLevel()
        );
    }

    public Employee findEmployeeByShopIdAndAccessCode(final Integer employmentShopId, final Integer accessCode) {
        final String sql = "SELECT * FROM employees WHERE employment_shop_id = :employmentShopId AND employee_access_code = :accessCode";
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("employmentShopId", employmentShopId)
                .addValue("accessCode", accessCode);

        try {
            Employee employee = getNamedParameterJdbcTemplate().queryForObject(sql, params, employeeRowMapper);
            return employee;
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Employee with access code " + accessCode + " not found");
        }
    }

    public Employee findById(final Integer employeeId) {
        final String sql = "SELECT * FROM employees WHERE employee_id = :employeeId";
        final MapSqlParameterSource params = new MapSqlParameterSource("employeeId", employeeId);

        try {
            return getNamedParameterJdbcTemplate().queryForObject(sql, params, employeeRowMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Employee with id " + employeeId + " not found");
        }
    }
}
