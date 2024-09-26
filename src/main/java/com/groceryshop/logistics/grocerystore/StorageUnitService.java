package com.groceryshop.logistics.grocerystore;

import org.springframework.stereotype.Service;

@Service
public class StorageUnitService {
    private final StorageUnitRepository storageUnitRepository;
    public StorageUnitService(final StorageUnitRepository storageUnitRepository) {
        this.storageUnitRepository = storageUnitRepository;
    }

    public StorageUnit createStorageUnit(final StorageUnitCreationDTO storageUnitCreationDTO) {
        return storageUnitRepository.createStorageUnit(storageUnitCreationDTO.storageUnitName());
    }

    public void updateStorageUnitName(final StorageUnit storageUnit) {
        storageUnitRepository.updateStorageUnitName(storageUnit.storageUnitId(), storageUnit.storageUnitName());
    }
    public void deleteByStorageUnitById(final Integer storageUnitId) {
        storageUnitRepository.deleteByStorageUnitById(storageUnitId);
    }
}
