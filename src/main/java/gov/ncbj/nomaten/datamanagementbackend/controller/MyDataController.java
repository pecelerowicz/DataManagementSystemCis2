package gov.ncbj.nomaten.datamanagementbackend.controller;

import com.google.gson.Gson;
import gov.ncbj.nomaten.datamanagementbackend.dto.shared_info_project.GetInfoResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.CreateStorageRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.CreateStorageResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.*;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.service.action.MyDataService;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_data.*;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.FileNameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.NameValidator;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static gov.ncbj.nomaten.datamanagementbackend.mapper.info.InfoMapper.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/api/my-data")
public class MyDataController {

    private final MyDataService myDataService;

    /**
     * LEFT PANEL
     */
    @GetMapping("/package")
    public ResponseEntity<GetPackageListResponse> getPackageList() {
        return ok(new GetPackageListResponse(myDataService.getPackageList()));
    }

    /**
     * LEFT PANEL
     */
    @PostMapping("/package")
    public ResponseEntity<CreatePackageResponse> createPackage(@RequestBody CreatePackageRequest createPackageRequest) throws IOException {
        CreatePackageRequestValidator.builder().build().validate(createPackageRequest);
        String createdPackageName = myDataService.createPackage(createPackageRequest.getPackageName());
        return ResponseEntity.status(CREATED).body(CreatePackageResponse.builder().createPackageMessage("Package " + createdPackageName + " was created").build());
    }

    /**
     * LEFT PANEL
     */
    @PutMapping("/package")
    public ResponseEntity<RenamePackageResponse> renamePackage(@RequestBody RenamePackageRequest renamePackageRequest) throws IOException {
        RenamePackageRequestValidator.builder().build().validate(renamePackageRequest);
        myDataService.renamePackage(renamePackageRequest);
        return ok(RenamePackageResponse.builder()
                .renamePackageMessage("The package " + renamePackageRequest.getPackageOldName() +
                                      " was renamed to " + renamePackageRequest.getPackageNewName()).build());
    }

    /**
     * LEFT PANEL
     */
    @DeleteMapping("/package")
    public ResponseEntity<DeletePackageResponse> deletePackage(@RequestBody DeletePackageRequest deletePackageRequest) throws IOException {
        DeletePackageRequestValidator.builder().build().validate(deletePackageRequest);
        myDataService.deletePackage(deletePackageRequest);
        return ok(DeletePackageResponse.builder().deleteMessage("The package " + deletePackageRequest.getPackageName() +
                                                                " was deleted").build());
    }

    /**
     * LEFT PANEL
     */
    @PutMapping("/archive")
    public ResponseEntity<ArchivePackageResponse> archivePackage(@RequestBody ArchivePackageRequest archivePackageRequest) {
        ArchivePackageRequestValidator.builder().build().validate(archivePackageRequest);
        myDataService.archivePackage(archivePackageRequest);
        return ok(ArchivePackageResponse.builder().archivePackageMessage("The package " + archivePackageRequest.getPackageName() +
                                                                         " was archived").build());
    }

    /**
     * LEFT PANEL
     */
    @GetMapping("/info/{infoName}")
    public ResponseEntity<GetInfoResponse> getInfo(@PathVariable String infoName) {
        NameValidator.builder().build().validate(infoName);
        return ok(infoToGetInfoResponse(myDataService.getInfo(infoName)));
    }

    /**
     * LEFT PANEL
     */
    @PostMapping("/info")
    public ResponseEntity<CreateInfoResponse> createInfo(@RequestBody CreateInfoRequest createInfoRequest) {
        CreateInfoRequestValidator.builder().build().validate(createInfoRequest);
        return ok(infoToCreateInfoResponse(myDataService.createInfo(createInfoRequest)));
    }

    /**
     * RIGHT PANEL
     */
    @PutMapping("/info")
    public ResponseEntity<UpdateInfoResponse> updateInfo(@RequestBody UpdateInfoRequest updateInfoRequest) {
        UpdateInfoRequestValidator.builder().build().validate(updateInfoRequest);
        return ok(infoToUpdateInfoResponse(myDataService.updateInfo(updateInfoRequest)));
    }

    /**
     * LEFT PANEL
     */
    @PostMapping("/storage")
    public ResponseEntity<CreateStorageResponse> createStorage(@RequestBody CreateStorageRequest createStorageRequest) throws IOException {
        CreateStorageRequestValidator.builder().build().validate(createStorageRequest);
        String createdStorageName = myDataService.createStorage(createStorageRequest.getStorageName());
        return ResponseEntity.status(CREATED).body(CreateStorageResponse.builder().createStorageMessage("Storage " + createdStorageName + " was created").build());
    }

    /**
     * LEFT PANEL
     */
    @GetMapping("/folders/{storageName}")
    public ResponseEntity<PathNode> getPackageFolderStructure(@PathVariable String storageName) {
        NameValidator.builder().build().validate(storageName);
        return ok(myDataService.getPackageFolderStructure(storageName));
    }

    /**
     * RIGHT PANEL
     */
    @PostMapping("/folders")
    public ResponseEntity<CreateFolderResponse> createFolder(@RequestBody CreateFolderRequest createFolderRequest) throws IOException {
        CreateFolderRequestValidator.builder().build().validate(createFolderRequest);
        return ok(new CreateFolderResponse(myDataService.createFolder(createFolderRequest)));
    }

    /**
     * RIGHT PANEL
     */
    @DeleteMapping("/folders")
    public ResponseEntity<DeleteFolderResponse> deleteItem(@RequestBody DeleteItemRequest deleteItemRequest) throws IOException {
        DeleteItemRequestValidator.builder().build().validate(deleteItemRequest);
        myDataService.deleteItem(deleteItemRequest);
        return ok(new DeleteFolderResponse("Item " + deleteItemRequest.getItemPathString() + " successfully deleted!"));
    }

    /**
     * RIGHT PANEL
     */
    @PostMapping("/upload")
    public ResponseEntity<UploadFileResponse> uploadFile(@RequestParam("file") MultipartFile multipartFile, @RequestParam("uploadFileRequest") String uploadFileRequestString) throws IOException{
        UploadFileRequest uploadFileRequest = new Gson().fromJson(uploadFileRequestString, UploadFileRequest.class);
        UploadFileRequestValidator.builder().build().validate(uploadFileRequest);
        FileNameValidator.builder().build().validate(multipartFile.getOriginalFilename());
        myDataService.uploadFile(multipartFile, uploadFileRequest.getPackageName(), uploadFileRequest.getFolderRelativePath());
        return ok(new UploadFileResponse("Successfully uploaded"));
    }

    /**
     * RIGHT PANEL
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String packageName, @RequestParam String fileNameWithPath) {
        NameValidator.builder().build().validate(packageName);
        FileNameWithPathValidator.builder().build().validate(fileNameWithPath);
        Resource resource = myDataService.downloadFile(packageName, fileNameWithPath);
        return ok()
//                .contentType(MediaType.parseMediaType(Files.probeContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .body(resource);
    }

}
