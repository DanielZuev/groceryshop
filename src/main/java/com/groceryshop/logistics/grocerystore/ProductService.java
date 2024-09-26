package com.groceryshop.logistics.grocerystore;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(final ProductCreationDTO product) {
        if (!productRepository.productNameExistsInShop(product.shopId(), product.productName())) {
            // Product does not exist, proceed to create
            return productRepository.createProduct(
                    product.shopId(),
                    product.productName(),
                    product.lowVolumeThreshold(),
                    product.metricType());
        } else {
            throw new RuntimeException("Product with name '" + product.productName() +
                    "' already exists in shop with ID " + product.shopId());
        }
    }


    public List<Product> productByNameFromShop(final Integer shopId, final String productName) {
        return productRepository.productByNameFromShop(shopId, productName);
    }

    public void deleteProductFromShop(final Integer shopId, final Integer productId) {
        productRepository.deleteProductFromShop(shopId, productId);
    }
}
