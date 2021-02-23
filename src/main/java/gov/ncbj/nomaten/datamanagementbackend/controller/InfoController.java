package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_storage.InfoListResponse;
import gov.ncbj.nomaten.datamanagementbackend.model.Info;
import gov.ncbj.nomaten.datamanagementbackend.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class InfoController {

    @Autowired
    InfoService infoService;

    @GetMapping("/info")
    public ResponseEntity<InfoListResponse> getInfoList() {
        return ResponseEntity.status(OK).body(new InfoListResponse(infoService.getInfoList()));
    }

}
