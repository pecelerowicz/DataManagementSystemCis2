package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_tem.DownloadZipFilesRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_tem.GrantAccessTemRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_tem.GrantAccessTemResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_tem.TemFolderStructure;
import gov.ncbj.nomaten.datamanagementbackend.service.action.TemService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/tem")
public class TemController {

    private final TemService temService;

    @GetMapping("/main-folder")
    public ResponseEntity<TemFolderStructure> getFolderStructure() {
        return ok(temService.getFolderStructure());
    }

    @GetMapping("/download-file")
    public ResponseEntity<Resource> downloadFile(@RequestParam String fileNameWithPath) {
        // todo validation
        Resource resource = temService.createFileResource(fileNameWithPath);
        return ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .body(resource);
    }

    @PostMapping("/download-zip-file")
    public ResponseEntity<Resource> downloadZipFile(@RequestParam String fileNameWithPath) {
        // todo validation (ONLY FILES WITH A VERY WELL DEFINED STRUCTURE CAN BE ZIPPED!)
        Resource resource = temService.createZipFileResource(fileNameWithPath);

        // todo finish
        return ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .body(resource);
    }

    @PostMapping("/download-zip-files")
    public ResponseEntity<Resource> downloadZipFiles(@RequestBody DownloadZipFilesRequest downloadZipFilesRequest) {
        // todo validation (ONLY FILES WITH A VERY WELL DEFINED STRUCTURE CAN BE ZIPPED!)
        Resource zipResource = temService.createZipFilesResource(downloadZipFilesRequest.getFileNamesWithPaths());

        return ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + zipResource.getFilename())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .body(zipResource);
    }

    @GetMapping("/download-zip-folder")
    public ResponseEntity<Resource> downloadZipFolder(@RequestParam String folderNameWithPath) {
        // todo validation
        Resource zipFolderResource = temService.createZipFolderResource(folderNameWithPath);

        return ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + zipFolderResource.getFilename())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .body(zipFolderResource);
    }

    @PutMapping("/grant-access")
    public ResponseEntity<GrantAccessTemResponse> grantAccessTem(@RequestBody GrantAccessTemRequest grantAccessTemRequest) {
        // todo validation
        return ok(temService.grantAccessTem(grantAccessTemRequest));
    }

}
