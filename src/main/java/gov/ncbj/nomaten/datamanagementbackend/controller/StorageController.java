package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;

import gov.ncbj.nomaten.datamanagementbackend.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@CrossOrigin
@RestController
@RequestMapping("/api")
public class StorageController {

    @Autowired
    private StorageService storageService;

    /**** PACKAGES ****/

    @GetMapping("/packages")
    public PathNode getPackages() {
        return storageService.getPackages();
    }

    //TODO zmienić ścieżkę, zrobić standardowe DTO (input i output)
    @PostMapping("/packages")
    public void createPackage(@RequestBody String newPackageName) throws IOException {
        storageService.create(newPackageName);
    }

    // TODO updatePackage (name for now)

    // TODO deletePackage

    /**** STORAGE ****/

    @GetMapping("/storage")
    public PathNode getStorage() {
        return storageService.getStorage();
    }

    // TODO createFolder

    // TODO updateFolder (name)

    // TODO deleteFolder (with subfolders) / deleteFile

    // TODO downloadFolder (with subfolders) / downloadFile

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadLocal(@RequestParam("file") MultipartFile multipartFile,
                                                       @RequestParam("internalPath") String relativePath) {
        storageService.upload(multipartFile, relativePath);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Successfully uploaded"));
    }

}

class ResponseMessage {
    private String message;

    public ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
