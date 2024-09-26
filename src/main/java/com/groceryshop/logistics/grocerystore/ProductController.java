package com.groceryshop.logistics.grocerystore;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;
    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(
            @RequestBody final ProductCreationDTO productDTO
    ) {
        try {
            final Product product = productService.createProduct(productDTO);
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping("/search")
    public List<Product> searchProductsByName(
            @RequestParam("shopId") final Integer shopId,
            @RequestParam("productName") final String productName
    ) {
        return productService.productByNameFromShop(shopId, productName);
    }

    @DeleteMapping("/{shopId}/{productId}")
    public ResponseEntity<Void> deleteProductFromShop(
            @PathVariable("shopId") final Integer shopId,
            @PathVariable("productId") final Integer productId) {

        productService.deleteProductFromShop(shopId, productId);
        // Return HTTP 204 No Content to indicate successful deletion
        return ResponseEntity.noContent().build();
    }
}
