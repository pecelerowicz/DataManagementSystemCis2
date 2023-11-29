package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.xrd.GrantAccessXrdRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.xrd.GrantAccessXrdResponse;
import gov.ncbj.nomaten.datamanagementbackend.model.XrdFolderStructure;
import gov.ncbj.nomaten.datamanagementbackend.service.XrdService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/xrd")
public class XrdController {

    private final XrdService xrdService;

    @GetMapping("/main-folder")
    public ResponseEntity<XrdFolderStructure> getXrdFolderStructure() {
        return ok(xrdService.getXrdFolderStructure());
    }

    @GetMapping("/download-file")
    public ResponseEntity<Resource> downloadXrdFile(@RequestParam String fileNameWithPath) {
        // todo validation
        Resource resource = xrdService.downloadXrdFile(fileNameWithPath);
        return ok()
//                .contentType(MediaType.parseMediaType(Files.probeContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .body(resource);
    }

    @PutMapping("/grant-access")
    public ResponseEntity<GrantAccessXrdResponse> grantAccessXrd(@RequestBody GrantAccessXrdRequest grantAccessXrdRequest) {
        // todo validation
        return ok(xrdService.grantAccessXrd(grantAccessXrdRequest));
    }

}
