package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.*;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.difrinfo.*;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.testinfo.*;
import gov.ncbj.nomaten.datamanagementbackend.service.InfoService;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_info.CreateInfoRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_info.UpdateInfoRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_info.GetInfoRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static gov.ncbj.nomaten.datamanagementbackend.mapper.DifrInfoMapper.*;
import static gov.ncbj.nomaten.datamanagementbackend.mapper.TestInfoMapper.*;
import static gov.ncbj.nomaten.datamanagementbackend.mapper.InfoMapper.*;
import static gov.ncbj.nomaten.datamanagementbackend.mapper.TestInfoMapper.testInfoToCreateTestInfoResponse;
import static gov.ncbj.nomaten.datamanagementbackend.mapper.TestInfoMapper.testInfoToGetTestInfoResponse;
import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin
@RestController
@RequestMapping("/api/info")
public class InfoController {

    private InfoService infoService;

    @Autowired
    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }

    // info
    @GetMapping("/")
    public ResponseEntity<GetInfoResponse> getInfo(@RequestBody GetInfoRequest getInfoRequest) {
        GetInfoRequestValidator.builder().build().validate(getInfoRequest);
        return ok(infoToGetInfoResponse(infoService.getInfo(getInfoRequest.getInfoName())));
    }

    @PostMapping(value = "/")
    public ResponseEntity<CreateInfoResponse> createInfo(@RequestBody CreateInfoRequest createInfoRequest) {
        CreateInfoRequestValidator.builder().build().validate(createInfoRequest);
        return ok(infoToCreateInfoResponse(infoService.createInfo(createInfoRequest)));
    }

    @PutMapping("/")
    public ResponseEntity<UpdateInfoResponse> updateInfo(@RequestBody UpdateInfoRequest updateInfoRequest) {
        UpdateInfoRequestValidator.builder().build().validate(updateInfoRequest);
        return ok(infoToUpdateInfoResponse(infoService.updateInfo(updateInfoRequest)));
    }

    // difrractometer info
    @GetMapping("/difr-info")
    public ResponseEntity<GetDifrInfoResponse> getDifrInfo(@RequestBody GetDifrInfoRequest getDifrInfoRequest) {
        return ok(difrInfoToGetDifrInfoResponse(infoService.getDifrInfo(getDifrInfoRequest)));
    }

    @PostMapping("/difr-info")
    public ResponseEntity<CreateDifrInfoResponse> createDifrInfo(@RequestBody CreateDifrInfoRequest createDifrInfoRequest) {
        return ok(difrInfoToCreateDifrInfoResponse(infoService.createDifrInfo(createDifrInfoRequest)));
    }

    @PutMapping("/difr-info")
    public ResponseEntity<UpdateDifrInfoResponse> updateDifrInfo(@RequestBody UpdateDifrInfoRequest updateDifrInfoRequest) {
        return ok(difrInfoToUpdateDifrInfoResponse(infoService.updateDifrInfo(updateDifrInfoRequest)));
    }

    @DeleteMapping("/difr-info")
    public ResponseEntity<DeleteDifrInfoResponse> deleteDifrInfo(@RequestBody DeleteDifrInfoRequest deleteDifrInfoRequest) {
        infoService.deleteDifrInfo(deleteDifrInfoRequest);
        return ok(DeleteDifrInfoResponse
                .builder()
                .infoName(deleteDifrInfoRequest.getInfoName())
                .message("Difr info " + deleteDifrInfoRequest.getInfoName() + " was deleted")
                .build());
    }

    // test info
    @GetMapping("/test-info")
    public ResponseEntity<GetTestInfoResponse> getTestInfo(@RequestBody GetTestInfoRequest getTestInfoRequest) {
        return ok(testInfoToGetTestInfoResponse(infoService.getTestInfo(getTestInfoRequest)));
    }

    @PostMapping("/test-info")
    public ResponseEntity<CreateTestInfoResponse> createTestInfo(@RequestBody CreateTestInfoRequest createTestInfoRequest) {
        return ok(testInfoToCreateTestInfoResponse(infoService.createTestInfo(createTestInfoRequest)));
    }

    @PutMapping("/test-info")
    public ResponseEntity<UpdateTestInfoResponse> updateTestInfo(@RequestBody UpdateTestInfoRequest updateTestInfoRequest) {
        return ok(testInfoToUpdateTestInfoResponse(infoService.updateTestInfo(updateTestInfoRequest)));
    }

    @DeleteMapping("/test-info")
    public ResponseEntity<DeleteTestInfoResponse> deleteTestInfo(@RequestBody DeleteTestInfoRequest deleteTestInfoRequest) {
        infoService.deleteTestInfo(deleteTestInfoRequest);
        return ok(DeleteTestInfoResponse
                .builder()
                .infoName(deleteTestInfoRequest.getInfoName())
                .message("Test info " + deleteTestInfoRequest.getInfoName() + " was deleted")
                .build());
    }

}
