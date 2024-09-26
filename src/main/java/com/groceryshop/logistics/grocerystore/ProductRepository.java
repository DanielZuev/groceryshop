package com.groceryshop.logistics.grocerystore;

import com.groceryshop.logistics.employees.Employee;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ProductRepository extends NamedParameterJdbcDaoSupport {

    private final RowMapper<Product> productRowMapper = (rs, rowNum) -> new Product(
            rs.getInt("product_id"),
            rs.getInt("shop_id"),
            rs.getString("product_name"),
            rs.getDouble("low_volume_threshold"),
            QuantityMetricType.valueOf(rs.getString("metric_type"))
    );

    public ProductRepository(final DataSource dataSource) {
        setDataSource(dataSource);
    }

    public Product createProduct(
            final Integer shopId,
            final String productName,
            final Double lowVolumeThreshold,
            final QuantityMetricType metricType
    ) {
        final String sql = "INSERT INTO products (shop_id, product_name, low_volume_threshold, metric_type) " +
                "VALUES (:shopId, :productName, :lowVolumeThreshold, :metricType)";
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("shopId", shopId)
                .addValue("productName", productName)
                .addValue("lowVolumeThreshold", lowVolumeThreshold)
                .addValue("metricType", metricType);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        getNamedParameterJdbcTemplate().update(sql, params, keyHolder, new String[] { "product_id" });

        final Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Failed to retrieve generated key");
        }

        return new Product(key.intValue(), shopId, productName, lowVolumeThreshold, metricType);
    }


    public boolean productNameExistsInShop(final Integer shopId, final String productName) {
        final String sql = "SELECT COUNT(*) FROM products WHERE shop_id = :shopId AND product_name = :productName";
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("shopId", shopId)
                .addValue("productName", productName);

        final Integer count = getNamedParameterJdbcTemplate().queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }


    public List<Product> productByNameFromShop(final Integer shopId, final String productName) {
        final String sql = "SELECT * FROM products WHERE shop_id = :shopId AND product_name LIKE :productName";
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("shopId", shopId)
                .addValue("productName", "%" + productName + "%");

        final List<Product> matchedProducts = getNamedParameterJdbcTemplate().query(sql, params, productRowMapper);
        return matchedProducts;
    }

    public void deleteProductFromShop(final Integer shopId, final Integer productId) {
        final String sql = "DELETE FROM products WHERE shop_id = :shopId AND product_id = :productId";
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("shopId", shopId)
                .addValue("productId", productId);

        final int rowsAffected = getNamedParameterJdbcTemplate().update(sql, params);
        if (rowsAffected == 0) {
            throw new RuntimeException(
                    "Failed to delete product from shop with ID " +
                            productId + " registered in shop with ID " + shopId);
        }
    }

}
