package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_storage.*;
import gov.ncbj.nomaten.datamanagementbackend.service.StorageService;
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
@RequestMapping("/api2")
public class StorageController2 {

    @Autowired
    private StorageService storageService;

    /**** PACKAGES ****/

    @GetMapping(value = "/packages",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PackagesResponse> getPackages() {
        return new ResponseEntity<>(pathNodeToPackagesResponse(storageService.getPackages()), OK);
    }

    @PostMapping(value = "/packages",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatePackageResponse> createPackage(
            @RequestBody @Valid CreatePackageRequest createPackageRequest) throws IOException {
        String createdFileName = storageService.create(createPackageRequest.getPackageName());
        return ResponseEntity.status(CREATED).body(new CreatePackageResponse(createdFileName));
    }
//
//    @PutMapping(value = "/packages",
//            consumes = APPLICATION_JSON_VALUE,
//            produces = APPLICATION_JSON_VALUE)
//    public ResponseEntity<UpdatePackageResponse> updatePackage(
//            @RequestBody @Valid UpdatePackageRequest updatePackageRequest) throws IOException {
//        String newPath = storageService.update(updatePackageRequest.getOldName(),
//                updatePackageRequest.getNewName());
//        return ResponseEntity.status(ACCEPTED).body(new UpdatePackageResponse(newPath));
//    }
//
//    @DeleteMapping(value = "/packages",
//            consumes = APPLICATION_JSON_VALUE,
//            produces = APPLICATION_JSON_VALUE)
//    public ResponseEntity<DeletePackageResponse> deletePackage(@RequestBody DeletePackageRequest deletePackageRequest) throws IOException {
//        storageService.delete(deletePackageRequest.getPackageName());
//        return ResponseEntity.status(OK)
//                .body(new DeletePackageResponse("The package "
//                        + deletePackageRequest.getPackageName() + " was deleted!"));
//    }

}
