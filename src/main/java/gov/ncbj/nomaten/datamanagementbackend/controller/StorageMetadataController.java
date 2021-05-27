package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_storage.*;
import gov.ncbj.nomaten.datamanagementbackend.service.StorageAndMetadataService;
import gov.ncbj.nomaten.datamanagementbackend.validators.CreateMetadataRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.CreatePackageRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
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

    @GetMapping(value = "/package",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PackagesResponse> getPackage() {
        return new ResponseEntity<>(pathNodeToPackagesResponse(storageAndMetadataService.getPackage()), OK);
    }

    @PostMapping(value = "/package",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatePackageResponse> createPackage(
            @RequestBody CreatePackageRequest createPackageRequest) throws IOException {
        new CreatePackageRequestValidator().validate(createPackageRequest);
        String createdStorageName = storageAndMetadataService.createPackage(createPackageRequest.getPackageName());
        return ResponseEntity.status(CREATED).body(new CreatePackageResponse(createdStorageName));
    }

    @PutMapping(value = "/package",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdatePackageResponse> updatePackage(
            @RequestBody @Valid UpdatePackageRequest updatePackageRequest) throws IOException {
        String newName = storageAndMetadataService.updatePackage(updatePackageRequest.getOldName(),
                updatePackageRequest.getNewName());
        return ResponseEntity.status(ACCEPTED).body(new UpdatePackageResponse(newName));
    }

    @DeleteMapping(value = "/package",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<DeletePackageResponse> deletePackage(@RequestBody DeletePackageRequest deletePackageRequest) throws IOException {
        storageAndMetadataService.deletePackage(deletePackageRequest.getPackageName());
        return ResponseEntity.status(OK)
                .body(new DeletePackageResponse("The package "
                        + deletePackageRequest.getPackageName() + " was deleted!"));
    }

    @GetMapping(value = "/storageMetadata")
    public ResponseEntity<StorageAndMetadataListResponse> getStorageAndMetadata() throws IOException {
        return ResponseEntity
                .status(OK)
                .body(new StorageAndMetadataListResponse(storageAndMetadataService.getStorageAndMetadataList()));
    }

    @PostMapping(value = "/storage")
    public ResponseEntity<CreateStorageResponse> createStorage(@RequestBody CreateStorageRequest createStorageRequest) throws IOException {
        CreateStorageResponse createStorageResponse = new CreateStorageResponse(storageAndMetadataService.createStorage(createStorageRequest.getStorageName()));
        return ResponseEntity.status(OK).body(createStorageResponse);
    }

    @PostMapping(value = "/metadata")
    public ResponseEntity<CreateMetadataResponse> createMetadata(@RequestBody CreateMetadataRequest createMetadataRequest) throws IOException{
        new CreateMetadataRequestValidator().validate(createMetadataRequest);
        CreateMetadataResponse createMetadataResponse = new CreateMetadataResponse(storageAndMetadataService.createMetadata(createMetadataRequest.getMetadataName()));
        return ResponseEntity.status(OK).body(createMetadataResponse);
    }

}
