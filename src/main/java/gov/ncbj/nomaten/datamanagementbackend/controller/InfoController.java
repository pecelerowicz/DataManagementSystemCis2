package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.subinfo.InfoDifrRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.subinfo.InfoDifrResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.subinfo.InfoTestRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.InfoDto;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.subinfo.InfoTestResponse;
import gov.ncbj.nomaten.datamanagementbackend.model.info.subinfo.DifrractometerInfo;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import gov.ncbj.nomaten.datamanagementbackend.model.info.subinfo.TestInfo;
import gov.ncbj.nomaten.datamanagementbackend.service.InfoService;
import gov.ncbj.nomaten.datamanagementbackend.validators.InfoDtoValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.InfoNameValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

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
    public ResponseEntity<InfoDto> getPackageInfo(@PathVariable String infoName) {
        new InfoNameValidator().validate(infoName);
        Info info = infoService.getInfo(infoName);
        InfoDto infoResponse = InfoDto
                .builder()
                .access(info.getAccess())
                .infoName(info.getInfoName())
                .shortName(info.getShortName())
                .longName(info.getLongName())
                .description(info.getDescription())
                .build();
        return ok(infoResponse);
    }

    @PutMapping("/package-info")
    public ResponseEntity<InfoDto> updatePackageInfo(@RequestBody InfoDto infoDto) {
        new InfoDtoValidator().validate(infoDto);
        Info info = infoService.updateInfo(infoDto);
        InfoDto infoResponse = InfoDto
                .builder()
                .infoName(info.getInfoName())
                .access(info.getAccess())
                .shortName(info.getShortName())
                .longName(info.getLongName())
                .description(info.getDescription())
                .build();
        return ok(infoResponse);
    }

    // difrractometer info
    @PostMapping("/package-info/difr-info")
    public ResponseEntity<InfoDifrResponse> addOrUpdateDifrractometerInfo(@RequestBody InfoDifrRequest infoDifrRequest) {
        DifrractometerInfo difrractometerInfo = infoService.addDifrractometerInfo(infoDifrRequest);
        return ok(InfoDifrResponse
                .builder()
                .infoName(difrractometerInfo.getInfo().getInfoName())
                .message("Difrractometer info added or updated")
                .build());
    }

    @DeleteMapping("/package-info/difr-info")
    public ResponseEntity<InfoDifrResponse> deleteDifrractometerInfo(@RequestBody InfoDifrRequest infoDifrRequest) {
        infoService.deleteDifrractometerInfo(infoDifrRequest);
        return ok(InfoDifrResponse
                .builder()
                .infoName(infoDifrRequest.getInfoName())
                .message("Difrractometer info deleted")
                .build());
    }

    // test info
    @PostMapping("/package-info/test-info")
    public ResponseEntity<InfoTestResponse> addOrUpdateTestInfo(@RequestBody InfoTestRequest infoTestRequest) {
        TestInfo testInfo = infoService.addTestInfo(infoTestRequest);
        return ok(InfoTestResponse
                .builder()
                .infoName(testInfo.getInfo().getInfoName())
                .message("Test info added or updated")
                .build());
    }

    @DeleteMapping("/package-info/test-info")
    public ResponseEntity<InfoTestResponse> deleteTestInfo(@RequestBody InfoTestRequest infoTestRequest) {
        infoService.deleteTestInfo(infoTestRequest);
        return ok(InfoTestResponse
                .builder()
                .infoName(infoTestRequest.getInfoName())
                .message("Test info deleted")
                .build());
    }

}
