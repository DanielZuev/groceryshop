CREATE TABLE shop_storage_units (
        shop_id INT NOT NULL,
        storage_unit_id INT NOT NULL,
        PRIMARY KEY (storage_unit_id, shop_id),
        FOREIGN KEY (shop_id) REFERENCES grocery_shops(shop_id) ON DELETE CASCADE,
        FOREIGN KEY (storage_unit_id) REFERENCES storage_units(storage_unit_id) ON DELETE CASCADE
);
