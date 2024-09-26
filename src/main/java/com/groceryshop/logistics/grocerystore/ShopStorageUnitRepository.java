package com.groceryshop.logistics.grocerystore;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ShopStorageUnitRepository extends NamedParameterJdbcDaoSupport {

    private final RowMapper<ShopStorageUnit> shopStorageUnitRowMapper = (rs, rowNum) ->
            new ShopStorageUnit(
                    rs.getInt("shop_id"),
                    rs.getInt("storage_unit_id")
            );
    private final RowMapper<StorageUnit> storageUnitRowMapper = (rs, rowNum) ->
            new StorageUnit(
                    rs.getInt("storage_unit_id"),
                    rs.getString("storage_unit_name")
            );
    public ShopStorageUnitRepository(final DataSource dataSource) {
        setDataSource(dataSource);
    }

    // Shop wants to add storage to their shop
    public ShopStorageUnit createShopStorageUnit(final Integer shopId, final Integer storageUnitId) {
        final String sql = "INSERT INTO shop_storage_units (shop_id, storage_unit_id) VALUES (:shopId, :storageUnitId)";
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("shopId", shopId)
                .addValue("storageUnitId", storageUnitId);

        try {
            getNamedParameterJdbcTemplate().update(sql, params);
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("This Storage Unit is already registered in shop with ID " + shopId);
        }

        return new ShopStorageUnit(shopId, storageUnitId);
    }

    // Shop wants to delete one of his Storages
    public void deleteByStorageUnitById(final Integer shopId, final Integer storageUnitId) {
        final String sql = "DELETE FROM shop_storage_units WHERE storage_unit_id = :storageUnitId AND shop_id = :shopId";
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("shopId", shopId)
                .addValue("storageUnitId", storageUnitId);

        int rowsAffected = getNamedParameterJdbcTemplate().update(sql, params);
        if (rowsAffected == 0) {
            throw new RuntimeException("Failed to delete storage unit with ID " + storageUnitId + " registered in shop with ID " + shopId);
        }
    }

    // Shop wants to retrieve all of his available Storages
    public List<StorageUnit> allStorageUnits(final Integer shopId) {
        final String sql = "SELECT su.* FROM storage_units su " +
                "INNER JOIN shop_storage_units ssu ON su.storage_unit_id = ssu.storage_unit_id " +
                "WHERE ssu.shop_id = :shopId";
        final MapSqlParameterSource params = new MapSqlParameterSource("shopId", shopId);
        final List<StorageUnit> storageUnits = getNamedParameterJdbcTemplate().query(sql, params, storageUnitRowMapper);
        if (storageUnits.isEmpty()) {
            throw new RuntimeException("No storage units found for shop with id " + shopId);
        }
        return storageUnits;
    }

}
