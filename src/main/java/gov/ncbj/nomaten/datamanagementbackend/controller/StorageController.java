package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_storage.*;
import gov.ncbj.nomaten.datamanagementbackend.service.StorageService;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_storage.CreateStorageRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_storage.DeleteStorageRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_storage.UpdateStorageRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static gov.ncbj.nomaten.datamanagementbackend.dto.my_storage.GetStorageListResponse.pathNodeToPackagesResponse;
import static org.springframework.http.HttpStatus.*;

@CrossOrigin
@RestController
@RequestMapping("/api/storage")
public class StorageController {

    private StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping
    public ResponseEntity<GetStorageListResponse> getStorageList() {
        return ResponseEntity.status(OK).body(pathNodeToPackagesResponse(storageService.getInfoListOfUser()));
    }

    @PostMapping
    public ResponseEntity<CreateStorageResponse> createStorage(@RequestBody CreateStorageRequest createStorageRequest) throws IOException {
        CreateStorageRequestValidator.builder().build().validate(createStorageRequest);
        String createdStorageName = storageService.createStorage(createStorageRequest.getStorageName());
        return ResponseEntity.status(CREATED).body(CreateStorageResponse.builder().createStorageMessage("Storage " + createdStorageName + " was created").build());
    }

    @PutMapping
    public ResponseEntity<UpdateStorageResponse> updateStorage(@RequestBody UpdateStorageRequest updateStorageRequest) throws IOException {
        UpdateStorageRequestValidator.builder().build().validate(updateStorageRequest);
        String newName = storageService.updateStorage(updateStorageRequest.getOldName(), updateStorageRequest.getNewName());
        return ResponseEntity.status(ACCEPTED).body(UpdateStorageResponse.builder().updateStorageMessage("Storage " + updateStorageRequest.getOldName() + " was renamed to " + newName).build());
    }

    @DeleteMapping
    public ResponseEntity<DeleteStorageResponse> deleteStorage(@RequestBody DeleteStorageRequest deleteStorageRequest) throws IOException {
        DeleteStorageRequestValidator.builder().build().validate(deleteStorageRequest);
        storageService.deleteStorage(deleteStorageRequest.getStorageName());
        return ResponseEntity.status(OK)
                .body(new DeleteStorageResponse("The storage " + deleteStorageRequest.getStorageName() + " was deleted"));
    }

}
