package com.groceryshop.logistics.grocerystore;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopStorageUnitService {
    private final StorageUnitRepository storageUnitRepository;
    private final ShopStorageUnitRepository shopStorageUnitRepository;

    public ShopStorageUnitService(final StorageUnitRepository storageUnitRepository, final ShopStorageUnitRepository shopStorageUnitRepository) {
        this.storageUnitRepository = storageUnitRepository;
        this.shopStorageUnitRepository = shopStorageUnitRepository;
    }

    public StorageUnit createShopStorageUnit(final String storageUnitName, final Integer shopId) {
        StorageUnit storageUnit = storageUnitRepository.createStorageUnit(storageUnitName);
        shopStorageUnitRepository.createShopStorageUnit(shopId, storageUnit.storageUnitId());
        return storageUnit;
    }

    public List<StorageUnit> allStorageUnits(final Integer shopId) {
        return shopStorageUnitRepository.allStorageUnits(shopId);
    }

    public void deleteByStorageUnitById(final Integer shopId, final Integer storageUnitId) {
        shopStorageUnitRepository.deleteByStorageUnitById(shopId, storageUnitId);
    }
}
