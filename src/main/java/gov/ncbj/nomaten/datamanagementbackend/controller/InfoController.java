package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.InfoDto;
import gov.ncbj.nomaten.datamanagementbackend.model.Info;
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

}
