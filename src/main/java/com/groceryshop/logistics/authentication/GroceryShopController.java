package com.groceryshop.logistics.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/groceryshop")
public class GroceryShopController {

    private final GroceryShopService groceryShopService;
    public GroceryShopController(GroceryShopService groceryShopService) {
        this.groceryShopService = groceryShopService;
    }

    @PostMapping("/register")
    public ResponseEntity<GroceryShop> registerGroceryShop(
            @RequestBody GroceryShopRequestDTO registerInfo
    ) {
        GroceryShop groceryShop = groceryShopService.registerGroceryShop(registerInfo);
        return new ResponseEntity<>(groceryShop, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<GroceryShop> loginGroceryShop(
            @RequestBody GroceryShopRequestDTO loginInfo
    ) {
        GroceryShop groceryShop = groceryShopService.loginGroceryShop(loginInfo);
        return new ResponseEntity<>(groceryShop, HttpStatus.ACCEPTED);
    }
}
