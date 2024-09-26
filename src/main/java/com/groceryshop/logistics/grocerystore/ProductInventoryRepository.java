package com.groceryshop.logistics.grocerystore;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ProductInventoryRepository extends NamedParameterJdbcDaoSupport {
    private final RowMapper<ProductInventory> productInventoryRowMapper = (rs, rowNum) ->
            new ProductInventory(
                    rs.getInt("storage_unit_id"),
                    rs.getInt("shop_id"),
                    rs.getInt("product_id"),
                    rs.getDouble("available_stock")
            );

    private final RowMapper<StorageUnit> storageUnitRowMapper = (rs, rowNum) ->
            new StorageUnit(
                    rs.getInt("storage_unit_id"),
                    rs.getString("storage_unit_name")
            );


    public ProductInventoryRepository(final DataSource dataSource) {
        setDataSource(dataSource);
    }

    public ProductInventory createProductInventory(final ProductInventory productInventory) {
        final String sql = "INSERT INTO products_inventory " +
                            "(storage_unit_id, shop_id, product_id, available_stock) " +
                            "VALUES " +
                            "(:storageUnitId, :shopId, :productId, :availableStock)";
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("storageUnitId", productInventory.storageUnitId())
                .addValue("shopId", productInventory.shopId())
                .addValue("productId", productInventory.productId())
                .addValue("availableStock", productInventory.availableStock());

        try {
            getNamedParameterJdbcTemplate().update(sql, params);
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("This products has been already added to the inventory list for this storage and shop ");
        }

        return new ProductInventory(productInventory.storageUnitId(), productInventory.shopId(), productInventory.productId(), productInventory.availableStock());
    }

    public void deleteProductsFromShopStorageUnit(final ProductInventory productInventory) {
        final String sql = "DELETE FROM products_inventory WHERE storage_unit_id = :storageUnitId AND shop_id = :shopId AND product_id = :productId";
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("storageUnitId", productInventory.storageUnitId())
                .addValue("shopId", productInventory.shopId())
                .addValue("productId", productInventory.productId());

        int rowsAffected = getNamedParameterJdbcTemplate().update(sql, params);
        if (rowsAffected == 0) {
            throw new RuntimeException(
                    "Failed to delete product from inventory with ID " +
                            productInventory.productId() +
                            " in shop with ID " +
                            productInventory.shopId() +
                            " and Storage Unit ID " +
                            productInventory.storageUnitId());
        }
    }

    public List<ProductInventory> allProductsByStorageAndShop(final Integer storageUnitId, final Integer shopId) {
        final String sql = "SELECT * FROM products_inventory WHERE storage_unit_id = :storageUnitId AND shop_id = :shopId";
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("storageUnitId", storageUnitId)
                .addValue("shopId", shopId);
        final List<ProductInventory> productInventories = getNamedParameterJdbcTemplate().query(sql, params, productInventoryRowMapper);
        if (productInventories.isEmpty()) {
            throw new RuntimeException("No products in storage unit with id " + storageUnitId + " found for shop with id " + shopId);
        }
        return productInventories;
    }

    // dali da vryshtam Map<storageId, List<ProductInventory>>
    public List<ProductInventory> allProductsByShop(final Integer shopId) {
        final String sql = "SELECT * FROM products_inventory WHERE shop_id = :shopId ORDER BY storage_unit_id";
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("shopId", shopId);
        final List<ProductInventory> productInventories = getNamedParameterJdbcTemplate().query(sql, params, productInventoryRowMapper);
        if (productInventories.isEmpty()) {
            throw new RuntimeException("No products in all storage found for shop with id " + shopId);
        }
        return productInventories;
    }

    public List<ProductInventory> productAvailableStockInStorageUnits(final Integer shopId, final Integer productId) {
        final String sql = "SELECT * FROM products_inventory WHERE shop_id = :shopId AND product_id = :productId";
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("shopId", shopId)
                .addValue("productId", productId);
        final List<ProductInventory> productInventories = getNamedParameterJdbcTemplate().query(sql, params, productInventoryRowMapper);
        if (productInventories.isEmpty()) {
            throw new RuntimeException("Product with ID " + productId + " was not found in any storage");
        }
        return productInventories;
    }

    public void updateStock(final ProductInventory productInventory) {
        final String sql = "UPDATE products_inventory SET available_stock = :newStock WHERE " +
                "storage_unit_id = :storageUnitId AND " +
                "shop_id = :shopId AND " +
                "product_id = :productId";
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("storageUnitId", productInventory.storageUnitId())
                .addValue("shopId", productInventory.shopId())
                .addValue("productId", productInventory.productId())
                .addValue("newStock", productInventory.availableStock());

        final int rowsAffected = getNamedParameterJdbcTemplate().update(sql, params);
        if (rowsAffected == 0) {
            throw new RuntimeException("Failed to update product stock with ID " + productInventory.productId());
        }
    }

    public boolean productExistsInInventory(final Integer storageUnitId, final Integer shopId, final Integer productId) {
        final String sql = "SELECT COUNT(*) FROM products_inventory WHERE storage_unit_id = :storageUnitId AND shop_id = :shopId AND product_id = :productId";
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("storageUnitId", storageUnitId)
                .addValue("shopId", shopId)
                .addValue("productId", productId);

        final Integer count = getNamedParameterJdbcTemplate().queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }
    public ProductInventory findProductInventory(final Integer storageUnitId, final Integer shopId, final Integer productId) {
        final String sql = "SELECT * FROM products_inventory WHERE storage_unit_id = :storageUnitId AND shop_id = :shopId AND product_id = :productId";
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("storageUnitId", storageUnitId)
                .addValue("shopId", shopId)
                .addValue("productId", productId);

        try {
            return getNamedParameterJdbcTemplate().queryForObject(sql, params, productInventoryRowMapper);
        } catch (EmptyResultDataAccessException e) {
            // No matching record found
            return null;
        }
    }

    public List<StorageUnit> storageUnitsContainingProduct(final Integer shopId, final Integer productId) {
        final String sql = "SELECT su.* FROM storage_units su " +
                "INNER JOIN products_inventory pi ON su.storage_unit_id = pi.storage_unit_id " +
                "WHERE pi.shop_id = :shopId AND pi.product_id = :productId";
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("shopId", shopId)
                .addValue("productId", productId);
        final List<StorageUnit> storageUnits = getNamedParameterJdbcTemplate().query(sql, params, storageUnitRowMapper);
        if (storageUnits.isEmpty()) {
            throw new RuntimeException("No storage units found containing product with ID " + productId + " in shop with ID " + shopId);
        }
        return storageUnits;
    }

    public List<StorageUnit> storageUnitsNotContainingProduct(final Integer shopId, final Integer productId) {
        final String sql = "SELECT su.* FROM storage_units su " +
                "INNER JOIN shop_storage_units ssu ON su.storage_unit_id = ssu.storage_unit_id " +
                "WHERE ssu.shop_id = :shopId " +
                "AND su.storage_unit_id NOT IN ( " +
                "    SELECT pi.storage_unit_id FROM products_inventory pi " +
                "    WHERE pi.shop_id = :shopId AND pi.product_id = :productId " +
                ")";
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("shopId", shopId)
                .addValue("productId", productId);

        final List<StorageUnit> storageUnits = getNamedParameterJdbcTemplate().query(sql, params, storageUnitRowMapper);
        if (storageUnits.isEmpty()) {
            throw new RuntimeException("No storage units found not containing product with ID " + productId + " in shop with ID " + shopId);
        }
        return storageUnits;
    }

}
