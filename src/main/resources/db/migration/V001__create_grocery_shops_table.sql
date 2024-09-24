CREATE TABLE grocery_shops (
       shop_id INT AUTO_INCREMENT PRIMARY KEY,
       shop_name VARCHAR(50) UNIQUE NOT NULL,
       shop_password VARCHAR(100) NOT NULL
);