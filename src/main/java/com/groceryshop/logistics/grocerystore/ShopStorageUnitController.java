package com.groceryshop.logistics.grocerystore;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/storageunits")
public class ShopStorageUnitController {
    private final ShopStorageUnitService shopStorageUnitService;
    public ShopStorageUnitController(final ShopStorageUnitService shopStorageUnitService) {
        this.shopStorageUnitService = shopStorageUnitService;
    }

    @PostMapping
    public ResponseEntity<StorageUnit> createShopStorageUnit(
            @RequestBody final ShopStorageUnitCreationDTO shopStorageUnitCreationDTO
    ) {
        final StorageUnit createdStorageUnit = shopStorageUnitService.createShopStorageUnit(shopStorageUnitCreationDTO.storageUnitName(), shopStorageUnitCreationDTO.shopId());
        return new ResponseEntity<>(createdStorageUnit, HttpStatus.CREATED);
    }

    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<StorageUnit>> getAllStorageUnitsByShop(
            @PathVariable final Integer shopId
    ) {
        final List<StorageUnit> storageUnits = shopStorageUnitService.allStorageUnits(shopId);
        return new ResponseEntity<>(storageUnits, HttpStatus.OK);
    }

    @DeleteMapping("/{storageUnitId}/shop/{shopId}")
    public ResponseEntity<Void> deleteStorageUnitById(
            @PathVariable final Integer storageUnitId,
            @PathVariable final Integer shopId
    ) {
        shopStorageUnitService.deleteByStorageUnitById(shopId, storageUnitId);
        return ResponseEntity.noContent().build();
    }

}
