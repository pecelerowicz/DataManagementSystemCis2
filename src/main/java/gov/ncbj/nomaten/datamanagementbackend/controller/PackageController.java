package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_package.*;
import gov.ncbj.nomaten.datamanagementbackend.service.PackageService;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_package.CreatePackageRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_package.DeletePackageRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.http.HttpStatus.*;

@CrossOrigin
@RestController
@RequestMapping("/api/package")
public class PackageController {

    @Autowired
    private PackageService packageService;

    @GetMapping
    public ResponseEntity<GetPackageListResponse> getPackageList() throws IOException {
        return ResponseEntity.status(OK).body(new GetPackageListResponse(packageService.getPackages()));
    }

    @PostMapping
    public ResponseEntity<CreatePackageResponse> createPackage(@RequestBody CreatePackageRequest createPackageRequest) throws IOException {
        CreatePackageRequestValidator.builder().build().validate(createPackageRequest);
        String createdPackageName = packageService.createPackage(createPackageRequest.getPackageName());
        return ResponseEntity.status(CREATED).body(CreatePackageResponse.builder().createPackageMessage("Package " + createdPackageName + " was created").build());
    }

    @DeleteMapping
    public ResponseEntity<DeletePackageResponse> deletePackage(@RequestBody DeletePackageRequest deletePackageRequest) throws IOException {
        DeletePackageRequestValidator.builder().build().validate(deletePackageRequest);
        packageService.deletePackage(deletePackageRequest);
        return ResponseEntity.status(OK).body(DeletePackageResponse.builder().deleteMessage(
                "The package " + deletePackageRequest.getPackageName() + " was deleted").build());
    }

}
