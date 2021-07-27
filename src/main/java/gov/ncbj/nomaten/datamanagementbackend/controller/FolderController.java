package gov.ncbj.nomaten.datamanagementbackend.controller;

import com.google.gson.Gson;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_folder.*;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;

import gov.ncbj.nomaten.datamanagementbackend.service.FolderService;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_folder.DownloadFileFileNameWithPathValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_folder.DownloadFilePackageNameValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;


@CrossOrigin
@RestController
@RequestMapping("/api")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @GetMapping("/package-folders/{storageName}")
    public PathNode getPackageFolderStructure(@PathVariable String storageName) {
        return folderService.getPackageFolderStructure(storageName);
    }

    @GetMapping("/full-folders")
    public PathNode getFullFolderStructure() {
        return folderService.getFullFolderStructure();
    }

    @PostMapping("/folders")
    public ResponseEntity<CreateFolderResponse> createFolder(@RequestBody CreateFolderRequest createFolderRequest) throws IOException {
        return ResponseEntity
                .status(OK)
                .body(new CreateFolderResponse(folderService.createFolder(createFolderRequest)));
    }

    @DeleteMapping(value = "/folders",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<DeleteFolderResponse> deleteFolder(@RequestBody DeleteFolderRequest deleteFolderRequest) throws IOException {
        folderService.deleteFolder(deleteFolderRequest.getPackageName(), deleteFolderRequest.getFolderPathString());
        return ResponseEntity
                .status(OK)
                .body(new DeleteFolderResponse("Folder "
                        + deleteFolderRequest.getFolderPathString() + " successfully deleted!"));
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadFileResponse> uploadFile(@RequestParam("file") MultipartFile multipartFile, @RequestParam("uploadFileRequest") String uploadFileRequestString) throws IOException{
        Gson gson = new Gson();
        UploadFileRequest uploadFileRequest = gson.fromJson(uploadFileRequestString, UploadFileRequest.class);
        String packageName = uploadFileRequest.getPackageName();
        String folderRelativePath = uploadFileRequest.getFolderRelativePath();
        folderService.uploadFile(multipartFile, packageName, folderRelativePath);
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

}
