package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.subinfo.DifrInfoDto;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.subinfo.TestInfoDto;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.InfoDto;
import gov.ncbj.nomaten.datamanagementbackend.mapper.InfoMapper;
import gov.ncbj.nomaten.datamanagementbackend.mapper.TestInfoMapper;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import gov.ncbj.nomaten.datamanagementbackend.model.info.subinfo.TestInfo;
import gov.ncbj.nomaten.datamanagementbackend.service.InfoService;
import gov.ncbj.nomaten.datamanagementbackend.validators.InfoDtoValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.InfoNameValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static gov.ncbj.nomaten.datamanagementbackend.mapper.DifrInfoMapper.difrInfoToDto;
import static gov.ncbj.nomaten.datamanagementbackend.mapper.InfoMapper.infoToDto;
import static gov.ncbj.nomaten.datamanagementbackend.mapper.TestInfoMapper.testInfoToDto;
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
        return ok(infoToDto(infoService.getInfo(infoName)));
    }

    @PutMapping("/package-info")
    public ResponseEntity<InfoDto> updatePackageInfo(@RequestBody InfoDto infoDto) {
        new InfoDtoValidator().validate(infoDto);
        return ok(infoToDto(infoService.updateInfo(infoDto)));
    }

    // difrractometer info
    @PostMapping("/package-info/difr-info")
    public ResponseEntity<DifrInfoDto> addOrUpdateDifrractometerInfo(@RequestBody DifrInfoDto difrInfoDto) {
        return ok(difrInfoToDto(infoService.addDifrractometerInfo(difrInfoDto)));
    }

    @DeleteMapping("/package-info/difr-info")
    public ResponseEntity<DifrInfoDto> deleteDifrractometerInfo(@RequestBody DifrInfoDto difrInfoDto) {
        infoService.deleteDifrractometerInfo(difrInfoDto);
        return ok(difrInfoDto);
    }

    // test info
    @PostMapping("/package-info/test-info")
    public ResponseEntity<TestInfoDto> addOrUpdateTestInfo(@RequestBody TestInfoDto testInfoDto) {
        TestInfo testInfo = infoService.addTestInfo(testInfoDto);
        return ok(testInfoToDto(testInfo));
    }

    @DeleteMapping("/package-info/test-info")
    public ResponseEntity<TestInfoDto> deleteTestInfo(@RequestBody TestInfoDto testInfoDto) {
        infoService.deleteTestInfo(testInfoDto);
        return ok(testInfoDto);
    }

}
