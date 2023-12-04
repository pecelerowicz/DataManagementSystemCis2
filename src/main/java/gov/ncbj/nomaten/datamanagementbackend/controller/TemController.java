package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_tem.GrantAccessTemRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_tem.GrantAccessTemResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_tem.TemFolderStructure;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_zip.DownloadTemZipFileRequest;
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
    public ResponseEntity<TemFolderStructure> getTemFolderStructure() {
        return ok(temService.getTemFolderStructure());
    }

    @GetMapping("/download-file")
    public ResponseEntity<Resource> downloadTemFile(@RequestParam String fileNameWithPath) {
        // todo validation
        Resource resource = temService.downloadTemFile(fileNameWithPath);
        return ok()
//                .contentType(MediaType.parseMediaType(Files.probeContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .body(resource);
    }

    @PostMapping("/download-zip-file")
    public ResponseEntity<Resource> downloadTemZipFile(@RequestBody DownloadTemZipFileRequest downloadTemZipFileRequest) {
        // todo validation (ONLY FILES WITH A VERY WELL DEFINED STRUCTURE CAN BE ZIPPED!)
        Resource resource = temService.createZipResource(downloadTemZipFileRequest.getFileNameWithPath());

        // todo finish
        return ok()
//                .contentType(MediaType.parseMediaType(Files.probeContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .body(resource);
    }

    @PutMapping("/grant-access")
    public ResponseEntity<GrantAccessTemResponse> grantAccessTem(@RequestBody GrantAccessTemRequest grantAccessTemRequest) {
        // todo validation
        return ok(temService.grantAccessTem(grantAccessTemRequest));
    }

}
