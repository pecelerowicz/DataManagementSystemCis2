package gov.ncbj.nomaten.datamanagementbackend.controller.refactor;

import com.google.gson.Gson;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_folder.*;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.*;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_package.*;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.service.FolderService;
import gov.ncbj.nomaten.datamanagementbackend.service.InfoService;
import gov.ncbj.nomaten.datamanagementbackend.service.PackageService;
import gov.ncbj.nomaten.datamanagementbackend.service.StorageService;
import gov.ncbj.nomaten.datamanagementbackend.validators.FileNameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_folder.*;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_info.CreateInfoRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_info.UpdateInfoRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_package.CreatePackageRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_package.DeletePackageRequestValidator;
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

    private final InfoService infoService;
    private final FolderService folderService;
    private final PackageService packageService;
    private final StorageService storageService;

    // get all packages
    @GetMapping("/package")
    public ResponseEntity<GetPackageListResponse> getPackageList() throws IOException {
        return ResponseEntity.status(OK).body(new GetPackageListResponse(packageService.getPackages()));
    }

    // create package
    @PostMapping("/package")
    public ResponseEntity<CreatePackageResponse> createPackage(@RequestBody CreatePackageRequest createPackageRequest) throws IOException {
        CreatePackageRequestValidator.builder().build().validate(createPackageRequest);
        String createdPackageName = packageService.createPackage(createPackageRequest.getPackageName());
        return ResponseEntity.status(CREATED).body(CreatePackageResponse.builder().createPackageMessage("Package " + createdPackageName + " was created").build());
    }

    // delete package (folder + metadata)
    @DeleteMapping("/package")
    public ResponseEntity<DeletePackageResponse> deletePackage(@RequestBody DeletePackageRequest deletePackageRequest) throws IOException {
        DeletePackageRequestValidator.builder().build().validate(deletePackageRequest);
        packageService.deletePackage(deletePackageRequest);
        return ResponseEntity.status(OK).body(DeletePackageResponse.builder().deleteMessage(
                "The package " + deletePackageRequest.getPackageName() + " was deleted").build());
    }

    // get metadata for a given package
    @GetMapping("/info/{infoName}")
    public ResponseEntity<GetInfoResponse> getInfo(@PathVariable String infoName) {
        NameValidator.builder().build().validate(infoName);
        return ok(infoToGetInfoResponse(infoService.getInfo(infoName)));
    }

    // create metadata for a given package
    @PostMapping("/info")
    public ResponseEntity<CreateInfoResponse> createInfo(@RequestBody CreateInfoRequest createInfoRequest) {
        CreateInfoRequestValidator.builder().build().validate(createInfoRequest);
        return ok(infoToCreateInfoResponse(infoService.createInfo(createInfoRequest)));
    }

    // modify metadata
    @PutMapping("/info")
    public ResponseEntity<UpdateInfoResponse> updateInfo(@RequestBody UpdateInfoRequest updateInfoRequest) {
        UpdateInfoRequestValidator.builder().build().validate(updateInfoRequest);
        return ok(infoToUpdateInfoResponse(infoService.updateInfo(updateInfoRequest)));
    }

    // get folder structure for a given package
    @GetMapping("/folders/{storageName}")
    public PathNode getPackageFolderStructure(@PathVariable String storageName) {
        NameValidator.builder().build().validate(storageName);
        return folderService.getPackageFolderStructure(storageName);
    }

    // create nested folder
    @PostMapping("/folders")
    public ResponseEntity<CreateFolderResponse> createNestedFolder(@RequestBody CreateFolderRequest createFolderRequest) throws IOException {
        CreateFolderRequestValidator.builder().build().validate(createFolderRequest);
        return ResponseEntity
                .status(OK)
                .body(new CreateFolderResponse(folderService.createFolder(createFolderRequest)));
    }

    // delete storage/file
    @DeleteMapping("/folders")
    public ResponseEntity<DeleteFolderResponse> deleteItem(@RequestBody DeleteItemRequest deleteItemRequest) throws IOException {
        DeleteItemRequestValidator.builder().build().validate(deleteItemRequest);
        folderService.deleteFolder(deleteItemRequest.getPackageName(), deleteItemRequest.getItemPathString());
        return ResponseEntity
                .status(OK)
                .body(new DeleteFolderResponse("Item " + deleteItemRequest.getItemPathString() + " successfully deleted!"));
    }


    @PostMapping("/upload")
    public ResponseEntity<UploadFileResponse> uploadFile(@RequestParam("file") MultipartFile multipartFile, @RequestParam("uploadFileRequest") String uploadFileRequestString) throws IOException{
        UploadFileRequest uploadFileRequest = new Gson().fromJson(uploadFileRequestString, UploadFileRequest.class);
        UploadFileRequestValidator.builder().build().validate(uploadFileRequest);
        FileNameValidator.builder().build().validate(multipartFile.getOriginalFilename());
        folderService.uploadFile(multipartFile, uploadFileRequest.getPackageName(), uploadFileRequest.getFolderRelativePath());
        return ResponseEntity.status(HttpStatus.OK).body(new UploadFileResponse("Successfully uploaded"));
    }

    // download from storage
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String packageName, @RequestParam String fileNameWithPath) {
        DownloadFilePackageNameValidator.builder().build().validate(packageName);
        DownloadFileFileNameWithPathValidator.builder().build().validate(fileNameWithPath);
        Resource resource = folderService.downloadFile(packageName, fileNameWithPath);
        return ok()
//                .contentType(MediaType.parseMediaType(Files.probeContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .body(resource);
    }


}
