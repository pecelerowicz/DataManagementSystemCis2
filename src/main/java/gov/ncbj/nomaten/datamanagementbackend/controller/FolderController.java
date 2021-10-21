package gov.ncbj.nomaten.datamanagementbackend.controller;

import com.google.gson.Gson;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_folder.*;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;

import gov.ncbj.nomaten.datamanagementbackend.service.FolderService;
import gov.ncbj.nomaten.datamanagementbackend.validators.FileNameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.UserNameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_folder.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.ok;


@CrossOrigin
@RestController
@RequestMapping("/api")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @GetMapping("/folders/{storageName}")
    public PathNode getPackageFolderStructure(@PathVariable String storageName) {
        NameValidator.builder().build().validate(storageName);
        return folderService.getPackageFolderStructure(storageName);
    }

    @GetMapping("/folders/{userName}/{storageName}")
    public PathNode getPackageFolderStructureOfUser(@PathVariable String userName, @PathVariable String storageName) {
        NameValidator.builder().build().validate(storageName);
        UserNameValidator.builder().build().validate(userName);
        return folderService.getPackageFolderStructureOfUser(userName, storageName);
    }

    @PostMapping("/folders")
    public ResponseEntity<CreateFolderResponse> createFolder(@RequestBody CreateFolderRequest createFolderRequest) throws IOException {
        CreateFolderRequestValidator.builder().build().validate(createFolderRequest);
        return ResponseEntity
                .status(OK)
                .body(new CreateFolderResponse(folderService.createFolder(createFolderRequest)));
    }

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

    @GetMapping("/download/user")
    public ResponseEntity<Resource> downloadFileOfUser(@RequestParam String userName, @RequestParam String packageName, @RequestParam String fileNameWithPath) {
        UserNameValidator.builder().build().validate(userName);
        NameValidator.builder().build().validate(packageName);
        // todo validate fileNameWithPath
        Resource resource = folderService.downloadFileOfUser(userName, packageName, fileNameWithPath);
        return ok()
//                .contentType(MediaType.parseMediaType(Files.probeContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .body(resource);
    }

    @GetMapping("/download/project")
    public ResponseEntity<Resource> downloadFileOfProject(@RequestParam String projectId, @RequestParam String userName, @RequestParam String infoName, @RequestParam String fileNameWithPath) {
        UserNameValidator.builder().build().validate(userName);
        NameValidator.builder().build().validate(infoName);
        // TODO validate remaining request params
        Resource resource = folderService.downloadFileOfProject(projectId, userName, infoName, fileNameWithPath);
        return ok()
//                .contentType(MediaType.parseMediaType(Files.probeContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .body(resource);
    }
}
