CREATE TABLE products (
       product_id INT AUTO_INCREMENT PRIMARY KEY,
       shop_id INT NOT NULL,
       product_name VARCHAR(100) NOT NULL,
       low_volume_threshold DOUBLE NOT NULL,
       metric_type ENUM('KILOGRAM', 'PIECE') NOT NULL,
       FOREIGN KEY (shop_id) REFERENCES grocery_shops(shop_id) ON DELETE CASCADE
);