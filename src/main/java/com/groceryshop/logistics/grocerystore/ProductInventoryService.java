package com.groceryshop.logistics.grocerystore;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductInventoryService {

    private final ProductInventoryRepository productInventoryRepository;
    private final ProductRepository productRepository;
    private final ShopStorageUnitRepository shopStorageUnitRepository;

    public ProductInventoryService(

            final ProductInventoryRepository productInventoryRepository,
            final ProductRepository productRepository,
            final ShopStorageUnitRepository shopStorageUnitRepository) {
        this.productInventoryRepository = productInventoryRepository;
        this.productRepository = productRepository;
        this.shopStorageUnitRepository = shopStorageUnitRepository;
    }

    public void addOrUpdateProductInventory(final ProductInventory productInventory) {
        ProductInventory existingProductInventory = productInventoryRepository.findProductInventory(
                productInventory.storageUnitId(),
                productInventory.shopId(),
                productInventory.productId()
        );

        if (existingProductInventory != null) {
            // Product exists; update stock
            // Calculate the new stock based on your business logic
            final double newStock = existingProductInventory.availableStock() + productInventory.availableStock(); // Adjust as needed

            // Create a new ProductInventory object with the updated stock
            final ProductInventory updatedProductInventory = new ProductInventory(
                    productInventory.storageUnitId(),
                    productInventory.shopId(),
                    productInventory.productId(),
                    newStock
            );

            // Update the stock in the repository
            productInventoryRepository.updateStock(updatedProductInventory);
        } else {
            // Product does not exist; create new inventory record
            productInventoryRepository.createProductInventory(productInventory);
        }
    }

    //metod da mestya stoka ot edin sklad kym drug s ideyata da zahranvam magazina
    @Transactional
    public void moveStock(final Integer shopId, final Integer productId,
                          final Integer fromStorageUnitId, final Integer toStorageUnitId,
                          final Double requestedAmount) {

        // Fetch the product inventory from both storage units
        ProductInventory takeFromStorageUnit = productInventoryRepository.findProductInventory(
                fromStorageUnitId, shopId, productId);
        ProductInventory giveToStorageUnit = productInventoryRepository.findProductInventory(
                toStorageUnitId, shopId, productId);

        if (takeFromStorageUnit == null) {
            throw new RuntimeException("Product not found in source storage unit.");
        }

        if (giveToStorageUnit == null) {
            // Initialize with zero stock if not present
            giveToStorageUnit = new ProductInventory(toStorageUnitId, shopId, productId, 0.0);
        }

        final Double availableStockToMove = takeFromStorageUnit.availableStock();

        if (requestedAmount <= availableStockToMove) {
            final Double newStockInSource = availableStockToMove - requestedAmount;

            // Update receiving storage unit
            final ProductInventory receivingStorageUnit = new ProductInventory(
                    giveToStorageUnit.storageUnitId(),
                    giveToStorageUnit.shopId(),
                    giveToStorageUnit.productId(),
                    requestedAmount + giveToStorageUnit.availableStock()
            );
            productInventoryRepository.updateStock(receivingStorageUnit);

            if (Double.compare(newStockInSource, 0.0) == 0) {
                // Delete the product inventory record from the source storage unit
                productInventoryRepository.deleteProductsFromShopStorageUnit(takeFromStorageUnit);
            } else {
                // Update the source storage unit with the new stock
                final ProductInventory givingStorageUnit = new ProductInventory(
                        takeFromStorageUnit.storageUnitId(),
                        takeFromStorageUnit.shopId(),
                        takeFromStorageUnit.productId(),
                        newStockInSource
                );
                productInventoryRepository.updateStock(givingStorageUnit);
            }
        } else {
            throw new RuntimeException("Not enough stock to move.");
        }
    }


    public List<ProductInventory> productAvailableStockInStorageUnits(final Integer shopId, final Integer productId) {
        return productInventoryRepository.productAvailableStockInStorageUnits(shopId, productId);
    }


        public List<Product> searchForProduct(final Integer shopId, final String productName) {
        List<Product> products = productRepository.productByNameFromShop(shopId, productName);
        return products;
    }

    public List<StorageUnit> storageUnitsContainingProduct(final Integer shopId, final Integer productId) {
        return productInventoryRepository.storageUnitsContainingProduct(shopId, productId);
    }

    public List<StorageUnit> storageUnitsNotContainingProduct(final Integer shopId, final Integer productId) {
        return productInventoryRepository.storageUnitsNotContainingProduct(shopId, productId);
    }

}
