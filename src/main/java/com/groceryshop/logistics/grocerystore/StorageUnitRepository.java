package com.groceryshop.logistics.grocerystore;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class StorageUnitRepository extends NamedParameterJdbcDaoSupport {

    private final RowMapper<StorageUnit> storageUnitRowMapper = (rs, rowNum) ->
            new StorageUnit(
                    rs.getInt("storage_unit_id"),
                    rs.getString("storage_unit_name")
            );
    public StorageUnitRepository(final DataSource dataSource) {
        setDataSource(dataSource);
    }

    public StorageUnit createStorageUnit(final String storageUnitName) {
        final String sql = "INSERT INTO storage_units (storage_unit_name) VALUES (:storageUnitName)";
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("storageUnitName", storageUnitName);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            getNamedParameterJdbcTemplate().update(sql, params, keyHolder, new String[] { "storage_unit_id" });
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("Storage Unit with name " + storageUnitName + " already exists");
        }
        final Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Failed to retrieve generated key");
        }

        return new StorageUnit(key.intValue(), storageUnitName);
    }

    public void updateStorageUnitName(final Integer storageUnitId, final String newStorageUnitName) {
        final String sql = "UPDATE storage_units SET storage_unit_name = :newStorageUnitName WHERE storage_unit_id = :storageUnitId";
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("storageUnitId", storageUnitId)
                .addValue("newStorageUnitName", newStorageUnitName);

        final int rowsAffected = getNamedParameterJdbcTemplate().update(sql, params);
        if (rowsAffected == 0) {
            throw new RuntimeException("Failed to update storage unit name with ID " + storageUnitId);
        }
    }

    public void deleteByStorageUnitById(final Integer storageUnitId) {
        final String sql = "DELETE FROM storage_units WHERE storage_unit_id = :storageUnitId";
        final MapSqlParameterSource params = new MapSqlParameterSource("storageUnitId", storageUnitId);

        int rowsAffected = getNamedParameterJdbcTemplate().update(sql, params);
        if (rowsAffected == 0) {
            throw new RuntimeException("Failed to delete storage unit with ID " + storageUnitId);
        }
    }

}
