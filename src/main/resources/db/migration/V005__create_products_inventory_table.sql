CREATE TABLE products_inventory (
       storage_unit_id INT NOT NULL,
       shop_id INT NOT NULL,
       product_id INT NOT NULL,
       availableStock DOUBLE NOT NULL,
       PRIMARY KEY (storage_unit_id, shop_id, product_id),
       FOREIGN KEY (storage_unit_id) REFERENCES storage_units(storage_unit_id) ON DELETE CASCADE,
       FOREIGN KEY (shop_id) REFERENCES grocery_shops(shop_id) ON DELETE CASCADE,
       FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);
