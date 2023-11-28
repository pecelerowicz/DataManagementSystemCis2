package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.model.XrdFolderStructure;
import gov.ncbj.nomaten.datamanagementbackend.service.XrdService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
