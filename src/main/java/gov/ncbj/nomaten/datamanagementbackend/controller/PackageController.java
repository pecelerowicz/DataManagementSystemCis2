package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_package.*;
import gov.ncbj.nomaten.datamanagementbackend.service.PackageService;
import gov.ncbj.nomaten.datamanagementbackend.validators.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.io.IOException;

import static gov.ncbj.nomaten.datamanagementbackend.dto.my_package.GetStorageListResponse.pathNodeToPackagesResponse;
import static org.springframework.http.HttpStatus.*;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class PackageController {

    @Autowired
    private PackageService packageService;

    @GetMapping(value = "/info")
    public ResponseEntity<GetInfoListResponse> getInfoList() {
        return ResponseEntity.status(OK).body(new GetInfoListResponse(packageService.getInfoList()));
    }

    @DeleteMapping(value = "/info")
    public ResponseEntity<DeleteInfoResponse> deleteInfo(@RequestBody DeleteInfoRequest deleteInfoRequest) {
        packageService.deleteInfo(deleteInfoRequest);
        return ResponseEntity.status(OK).body(DeleteInfoResponse
                .builder()
                .deleteMessage("Info " + deleteInfoRequest.getInfoName() + " was deleted")
                .build());
    }

    @GetMapping(value = "/storage")
    public ResponseEntity<GetStorageListResponse> getStorageList() {
        return ResponseEntity.status(OK).body(pathNodeToPackagesResponse(packageService.getStorageList()));
    }

    @PostMapping(value = "/storage")
    public ResponseEntity<CreateStorageResponse> createStorage(
            @RequestBody CreateStorageRequest createStorageRequest) throws IOException {
        new CreateStorageRequestValidator().validate(createStorageRequest);
        String createdStorageName = packageService.createStorage(createStorageRequest.getStorageName());
        return ResponseEntity.status(CREATED).body(new CreateStorageResponse(createdStorageName));
    }

    @PutMapping(value = "/storage")
    public ResponseEntity<UpdateStorageResponse> updateStorage(
            @RequestBody UpdateStorageRequest updateStorageRequest) throws IOException {
        new UpdateStorageRequestValidator().validate(updateStorageRequest);
        String newName = packageService.updateStorage(updateStorageRequest.getOldName(),
                updateStorageRequest.getNewName());
        return ResponseEntity.status(ACCEPTED).body(new UpdateStorageResponse(newName));
    }

    @DeleteMapping(value = "/storage")
    public ResponseEntity<DeleteStorageResponse> deleteStorage(@RequestBody DeleteStorageRequest deleteStorageRequest) throws IOException {
        new DeleteStorageRequestValidator().validate(deleteStorageRequest);
        packageService.deleteStorage(deleteStorageRequest.getStorageName());
        return ResponseEntity.status(OK)
                .body(new DeleteStorageResponse("The storage "
                        + deleteStorageRequest.getStorageName() + " was deleted!"));
    }

    @GetMapping(value = "/package")
    public ResponseEntity<GetPackageListResponse> getPackageList() throws IOException {
        return ResponseEntity.status(OK).body(new GetPackageListResponse(packageService.getPackages()));
    }

    @DeleteMapping(value = "/package")
    public ResponseEntity<DeletePackageResponse> deletePackage(@RequestBody DeletePackageRequest deletePackageRequest) throws IOException {
        packageService.deletePackage(deletePackageRequest);
        return ResponseEntity.status(OK).body(DeletePackageResponse.builder().deleteMessage(
                "The package " + deletePackageRequest.getPackageName() + " was deleted!").build());
    }

}
