package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.InfoResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.UpdateInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.UpdateInfoResponse;
import gov.ncbj.nomaten.datamanagementbackend.model.Info;
import gov.ncbj.nomaten.datamanagementbackend.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class InfoController {

    private InfoService infoService;

    @Autowired
    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }

    @GetMapping("/package-info/{infoName}")
    public ResponseEntity<InfoResponse> getPackageInfo(@PathVariable String infoName) {
        Info info = infoService.getInfo(infoName);
        InfoResponse infoResponse = InfoResponse
                .builder()
                .name(info.getName())
                .access(info.getAccess())
                .shortName(info.getShortName())
                .longName(info.getLongName())
                .build();
        return ResponseEntity.ok(infoResponse);
    }

    @PutMapping("/package-info")
    public ResponseEntity<UpdateInfoResponse> updatePackageInfo(@RequestBody UpdateInfoRequest updateInfoRequest) {
        Info info = infoService.updateInfo(updateInfoRequest);
        UpdateInfoResponse updateInfoResponse = UpdateInfoResponse
                .builder()
                .name(info.getName())
                .access(info.getAccess())
                .shortName(info.getShortName())
                .longName(info.getLongName())
                .build();
        return ResponseEntity.ok(updateInfoResponse);
    }

}
