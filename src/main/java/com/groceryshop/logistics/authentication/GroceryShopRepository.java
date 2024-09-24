package com.groceryshop.logistics.authentication;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class GroceryShopRepository extends NamedParameterJdbcDaoSupport {

    private RowMapper<GroceryShop> shopRowMapper = (rs, rowNum) -> new GroceryShop(
            rs.getInt("shop_id"),
            rs.getString("shop_name"),
            rs.getString("shop_password")
    );
    public GroceryShopRepository(final DataSource dataSource) {
        setDataSource(dataSource);
    }
    public GroceryShop registerGroceryShop(final GroceryShop groceryShop) {
        final String sql = "INSERT INTO grocery_shops (shop_name, shop_password) VALUES (:shopName, :shopPassword)";
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("shopName", groceryShop.shopName())
                .addValue("shopPassword", groceryShop.shopPassword());
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            getNamedParameterJdbcTemplate().update(sql, params, keyHolder, new String[] { "shop_id" });
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("Shop with shop name " + groceryShop.shopName() + " already exists");
        }
        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Failed to retrieve generated key");
        }

        return new GroceryShop(key.intValue(), groceryShop.shopName(), groceryShop.shopPassword());
    }

    public GroceryShop findShopByName(final String shopName) {
        final String sql = "SELECT * FROM grocery_shops WHERE shop_name = :shopName";
        final MapSqlParameterSource params = new MapSqlParameterSource("shopName", shopName);

        try {
            GroceryShop groceryShop = getNamedParameterJdbcTemplate().queryForObject(sql, params, shopRowMapper);
            return groceryShop;
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Grocery shop with username " + shopName + " not found");
        }
    }

    public GroceryShop findById(int shopId) {
        final String sql = "SELECT * FROM grocery_shops WHERE shop_id = :shopId";
        final MapSqlParameterSource params = new MapSqlParameterSource("shopId", shopId);
        try {
            return getNamedParameterJdbcTemplate().queryForObject(sql, params, shopRowMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Grocery shop with id " + shopId + " not found");
        }
    }
}
