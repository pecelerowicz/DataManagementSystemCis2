package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_storage.*;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.service.StorageAndMetadataService;
import gov.ncbj.nomaten.datamanagementbackend.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

import static gov.ncbj.nomaten.datamanagementbackend.dto.my_storage.PackagesResponse.pathNodeToPackagesResponse;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class StorageMetadataController {

    @Autowired
    private StorageAndMetadataService storageAndMetadataService;

    @GetMapping(value = "/storage",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PackagesResponse> getStorage() {
        return new ResponseEntity<>(pathNodeToPackagesResponse(storageAndMetadataService.getStorage()), OK);
    }

    @PostMapping(value = "/storage",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatePackageResponse> createStorage(
            @RequestBody @Valid CreatePackageRequest createPackageRequest) throws IOException {
        String createdStorageName = storageAndMetadataService.createStorage(createPackageRequest.getPackageName());
        return ResponseEntity.status(CREATED).body(new CreatePackageResponse(createdStorageName));
    }

    @PutMapping(value = "/storage",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdatePackageResponse> updateStorage(
            @RequestBody @Valid UpdatePackageRequest updatePackageRequest) throws IOException {
        String newName = storageAndMetadataService.updateStorage(updatePackageRequest.getOldName(),
                updatePackageRequest.getNewName());
        return ResponseEntity.status(ACCEPTED).body(new UpdatePackageResponse(newName));
    }

    @DeleteMapping(value = "/storage",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<DeletePackageResponse> deleteStorage(@RequestBody DeletePackageRequest deletePackageRequest) throws IOException {
        storageAndMetadataService.deleteStorage(deletePackageRequest.getPackageName());
        return ResponseEntity.status(OK)
                .body(new DeletePackageResponse("The package "
                        + deletePackageRequest.getPackageName() + " was deleted!"));
    }

    @GetMapping(value = "/storageMetadata")
    public ResponseEntity<StorageAndMetadataResponse> getStorageAndMetadata() throws IOException {
        return ResponseEntity
                .status(OK)
                .body(new StorageAndMetadataResponse(storageAndMetadataService.getStorageAndMetadata()));
    }

}
