package com.groceryshop.logistics.authentication;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GroceryShopService {
    private final GroceryShopRepository groceryShopRepository;
    private final BCryptPasswordEncoder encoder;

    public GroceryShopService(GroceryShopRepository groceryShopRepository) {
        this.groceryShopRepository = groceryShopRepository;
        this.encoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public GroceryShop registerGroceryShop(final GroceryShopRequestDTO registerInfo) {
        final String hashedPassword = encoder.encode(registerInfo.shopPassword());
        final GroceryShop groceryShopToRegister = new GroceryShop(null, registerInfo.shopName(), hashedPassword);
        return groceryShopRepository.registerGroceryShop(groceryShopToRegister);
    }

    @Transactional
    public GroceryShop loginGroceryShop(final GroceryShopRequestDTO loginInfo) {
        // Retrieve the user from the database
        final GroceryShop storedGroceryShop = groceryShopRepository.findShopByName(loginInfo.shopName());

        // Check if user exists
        if (storedGroceryShop == null) {
            throw new RuntimeException("Wrong login details");
        }

        // Compare raw password with stored hashed password
        if (encoder.matches(loginInfo.shopPassword(), storedGroceryShop.shopPassword())) {
            return storedGroceryShop;
        } else {
            throw new RuntimeException("Wrong login details");
        }
    }
}
