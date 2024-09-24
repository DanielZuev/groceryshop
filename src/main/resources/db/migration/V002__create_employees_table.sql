CREATE TABLE employees (
     employee_id INT AUTO_INCREMENT PRIMARY KEY,
     employment_shop_id INT NOT NULL,
     employee_firstname VARCHAR(255) NOT NULL,
     employee_lastname VARCHAR(255) NOT NULL,
     employee_position VARCHAR(255) NOT NULL,
     employee_access_code INT NOT NULL,
     employee_access_level ENUM('USER', 'ADMIN') NOT NULL,
     FOREIGN KEY (employment_shop_id) REFERENCES grocery_shops(shop_id) ON DELETE CASCADE
);
