package gov.ncbj.nomaten.datamanagementbackend.controller;

import com.google.gson.Gson;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_folder.*;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.*;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_package.*;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_storage.CreateStorageRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_storage.CreateStorageResponse;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.service.main.MyDataService;
import gov.ncbj.nomaten.datamanagementbackend.validators.FileNameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_folder.*;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_info.CreateInfoRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_info.UpdateInfoRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_package.CreatePackageRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_package.DeletePackageRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_storage.CreateStorageRequestValidator;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static gov.ncbj.nomaten.datamanagementbackend.mapper.info.InfoMapper.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
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
    public ResponseEntity<GetPackageListResponse> getPackageList() throws IOException {
        return ResponseEntity.status(OK).body(new GetPackageListResponse(myDataService.getPackageList()));
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
    @DeleteMapping("/package")
    public ResponseEntity<DeletePackageResponse> deletePackage(@RequestBody DeletePackageRequest deletePackageRequest) throws IOException {
        DeletePackageRequestValidator.builder().build().validate(deletePackageRequest);
        myDataService.deletePackage(deletePackageRequest);
        return ResponseEntity.status(OK).body(DeletePackageResponse.builder().deleteMessage(
                "The package " + deletePackageRequest.getPackageName() + " was deleted").build());
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
    public PathNode getPackageFolderStructure(@PathVariable String storageName) {
        NameValidator.builder().build().validate(storageName);
        return myDataService.getPackageFolderStructure(storageName);
    }

    /**
     * RIGHT PANEL
     */
    @PostMapping("/folders")
    public ResponseEntity<CreateFolderResponse> createFolder(@RequestBody CreateFolderRequest createFolderRequest) throws IOException {
        CreateFolderRequestValidator.builder().build().validate(createFolderRequest);
        return ResponseEntity
                .status(OK)
                .body(new CreateFolderResponse(myDataService.createFolder(createFolderRequest)));
    }

    /**
     * RIGHT PANEL
     */
    @DeleteMapping("/folders")
    public ResponseEntity<DeleteFolderResponse> deleteItem(@RequestBody DeleteItemRequest deleteItemRequest) throws IOException {
        DeleteItemRequestValidator.builder().build().validate(deleteItemRequest);
        myDataService.deleteItem(deleteItemRequest.getPackageName(), deleteItemRequest.getItemPathString());
        return ResponseEntity
                .status(OK)
                .body(new DeleteFolderResponse("Item " + deleteItemRequest.getItemPathString() + " successfully deleted!"));
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
        return ResponseEntity.status(HttpStatus.OK).body(new UploadFileResponse("Successfully uploaded"));
    }

    /**
     * RIGHT PANEL
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String packageName, @RequestParam String fileNameWithPath) {
        DownloadFilePackageNameValidator.builder().build().validate(packageName);
        DownloadFileFileNameWithPathValidator.builder().build().validate(fileNameWithPath);
        Resource resource = myDataService.downloadFile(packageName, fileNameWithPath);
        return ok()
//                .contentType(MediaType.parseMediaType(Files.probeContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .body(resource);
    }

}
