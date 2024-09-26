package com.groceryshop.logistics.grocerystore;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/storageunitsbroker")
public class StorageUnitController {
    private final StorageUnitService storageUnitService;
    public StorageUnitController(final StorageUnitService storageUnitService) {
        this.storageUnitService = storageUnitService;
    }
    @PostMapping
    public ResponseEntity<StorageUnit> createProduct(
            @RequestBody final StorageUnitCreationDTO storageUnitDTO
    ) {
        final StorageUnit storageUnit = storageUnitService.createStorageUnit(storageUnitDTO);
        return new ResponseEntity<>(storageUnit, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> updateStorageUnitName(
            @RequestBody final StorageUnit storageUnit
    ) {
        storageUnitService.updateStorageUnitName(storageUnit);

        // Return HTTP 204 No Content to indicate success
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{storageUnitId}")
    public ResponseEntity<Void> deleteStorageUnitById(
            @PathVariable("storageUnitId") final Integer storageUnitId
    ) {
        storageUnitService.deleteByStorageUnitById(storageUnitId);

        // Return HTTP 204 No Content to indicate success
        return ResponseEntity.noContent().build();
    }


}
