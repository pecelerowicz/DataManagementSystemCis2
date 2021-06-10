package gov.ncbj.nomaten.datamanagementbackend.controller;

import com.google.gson.Gson;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_storage.*;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;

import gov.ncbj.nomaten.datamanagementbackend.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


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

    @PostMapping(value = "/folders",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateFolderResponse> createFolder(@RequestBody CreateFolderRequest createFolderRequest) throws IOException {
        String newFolderName = createFolderRequest.getNewFolderName();
        String packageName = createFolderRequest.getPackageName();
        String parentFolderRelativePath = createFolderRequest.getParentFolderRelativePath() == null
                ? "" : createFolderRequest.getParentFolderRelativePath();

        return ResponseEntity
                .status(OK)
                .body(new CreateFolderResponse(folderService.createFolder(newFolderName, packageName, parentFolderRelativePath)));
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
    public ResponseEntity<UploadFileResponse> uploadLocal(@RequestParam("file") MultipartFile multipartFile, @RequestParam("uploadFileRequest") String uploadFileRequestString) throws IOException{
        Gson gson = new Gson();
        UploadFileRequest uploadFileRequest = gson.fromJson(uploadFileRequestString, UploadFileRequest.class);
        String packageName = uploadFileRequest.getPackageName();
        String folderRelativePath = uploadFileRequest.getFolderRelativePath();
        folderService.uploadFile(multipartFile, packageName, folderRelativePath);
        return ResponseEntity.status(HttpStatus.OK).body(new UploadFileResponse("Successfully uploaded"));
    }

}
