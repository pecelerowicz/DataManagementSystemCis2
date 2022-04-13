package gov.ncbj.nomaten.datamanagementbackend.controller.refactor;

import gov.ncbj.nomaten.datamanagementbackend.service.FolderService;
import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.UserNameValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/my-projects")
public class MyProjectsController {
    @Autowired
    private FolderService folderService;

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFileOfProject(@RequestParam String projectId, @RequestParam String userName, @RequestParam String infoName, @RequestParam String fileNameWithPath) {
        UserNameValidator.builder().build().validate(userName);
        NameValidator.builder().build().validate(infoName);
        // TODO validate remaining request params
        Resource resource = folderService.downloadFileOfProject(projectId, userName, infoName, fileNameWithPath);
        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(Files.probeContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .body(resource);
    }
}
