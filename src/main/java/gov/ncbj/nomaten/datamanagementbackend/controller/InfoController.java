package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.DeviceDto;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.InfoDto;
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
    public ResponseEntity<InfoDto> getPackageInfo(@PathVariable String infoName) {
        Info info = infoService.getInfo(infoName);
        InfoDto infoResponse = InfoDto
                .builder()
                .access(info.getAccess())
                .infoName(info.getInfoName())
                .shortName(info.getShortName())
                .longName(info.getLongName())
                .deviceDto(DeviceDto
                        .builder()
                        .name(info.getDevice().getName())
                        .build())
                .build();
        return ResponseEntity.ok(infoResponse);
    }

    @PutMapping("/package-info")
    public ResponseEntity<InfoDto> updatePackageInfo(@RequestBody InfoDto infoDto) {
        Info info = infoService.updateInfo(infoDto);
        InfoDto infoResponse = InfoDto
                .builder()
                .infoName(info.getInfoName())
                .access(info.getAccess())
                .shortName(info.getShortName())
                .longName(info.getLongName())
                .deviceDto(DeviceDto
                        .builder()
                        .name(info.getDevice().getName())
                        .build())
                .build();
        return ResponseEntity.ok(infoResponse);
    }

}
