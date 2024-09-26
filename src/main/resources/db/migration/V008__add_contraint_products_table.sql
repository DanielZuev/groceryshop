ALTER TABLE products
    ADD CONSTRAINT uc_shop_product UNIQUE (shop_id, product_name);
