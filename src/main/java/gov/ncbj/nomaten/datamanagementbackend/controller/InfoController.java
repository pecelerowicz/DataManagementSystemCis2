package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.AddUpdateDifrractometerInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.AddUpdateTestInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.SubInfoDto;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.InfoDto;
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
    @PostMapping("/package-info/dif-info")
    public ResponseEntity<SubInfoDto> addOrUpdateDifrractometerInfo(@RequestBody AddUpdateDifrractometerInfoRequest difrractometerInfoRequest) {
        DifrractometerInfo difrractometerInfo = infoService.addDifrractometerInfo(difrractometerInfoRequest);
        return ok(SubInfoDto
                .builder()
                .infoName(difrractometerInfo.getInfo().getInfoName())
                .message("Difrractometer info added/updated")
                .build());
    }

    @DeleteMapping("/package-info/dif-info")
    public ResponseEntity<SubInfoDto> deleteDifrractometerInfo(@RequestBody SubInfoDto subInfoDto) {
        infoService.deleteDifrractometerInfo(subInfoDto);
        return ok(SubInfoDto
                .builder()
                .infoName(subInfoDto.getInfoName())
                .message("Difrractometer info deleted")
                .build());
    }

    // test info
    @PostMapping("/package-info/test-info")
    public ResponseEntity<SubInfoDto> addOrUpdateTestInfo(@RequestBody AddUpdateTestInfoRequest testInfoRequest) {
        TestInfo testInfo = infoService.addTestInfo(testInfoRequest);
        return ok(SubInfoDto
                .builder()
                .infoName(testInfo.getInfo().getInfoName())
                .message("Test info added/updated")
                .build());
    }

    @DeleteMapping("/package-info/test-info")
    public ResponseEntity<SubInfoDto> deleteTestInfo(@RequestBody SubInfoDto subInfoDto) {
        infoService.deleteTestInfo(subInfoDto);
        return ok(SubInfoDto
                .builder()
                .infoName(subInfoDto.getInfoName())
                .message("Test info deleted")
                .build());
    }

}
