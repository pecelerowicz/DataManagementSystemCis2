package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.*;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.difrinfo.*;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.testinfo.*;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.DeleteInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.DeleteInfoResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_package.GetInfoListResponse;
import gov.ncbj.nomaten.datamanagementbackend.service.InfoService;
import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.UserNameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_info.CreateInfoRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_info.UpdateInfoRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_info.DeleteInfoRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_info.difr_info.CreateDifrInfoRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_info.difr_info.DeleteDifrInfoRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_info.difr_info.UpdateDifrInfoRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_info.test_info.CreateTestInfoRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_info.test_info.UpdateTestInfoRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static gov.ncbj.nomaten.datamanagementbackend.mapper.DifrInfoMapper.*;
import static gov.ncbj.nomaten.datamanagementbackend.mapper.TestInfoMapper.*;
import static gov.ncbj.nomaten.datamanagementbackend.mapper.InfoMapper.*;
import static gov.ncbj.nomaten.datamanagementbackend.mapper.TestInfoMapper.testInfoToCreateTestInfoResponse;
import static gov.ncbj.nomaten.datamanagementbackend.mapper.TestInfoMapper.testInfoToGetTestInfoResponse;
import static org.springframework.http.HttpStatus.OK;
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
    @GetMapping("/{infoName}")
    public ResponseEntity<GetInfoResponse> getInfo(@PathVariable String infoName) {
        NameValidator.builder().build().validate(infoName);
        return ok(infoToGetInfoResponse(infoService.getInfo(infoName)));
    }

    @GetMapping("/{userName}/{infoName}")
    public ResponseEntity<GetInfoResponse> getInfoOfUser(@PathVariable String userName, @PathVariable String infoName) {
        UserNameValidator.builder().build().validate(userName);
        NameValidator.builder().build().validate(infoName);
        return ok(infoToGetInfoResponse(infoService.getInfoOfUser(userName, infoName)));
    }

    @GetMapping
    public ResponseEntity<GetInfoListResponse> getInfoList() {
        return ResponseEntity.status(OK).body(new GetInfoListResponse(infoService.getInfoList()));
    }

    @PostMapping
    public ResponseEntity<CreateInfoResponse> createInfo(@RequestBody CreateInfoRequest createInfoRequest) {
        CreateInfoRequestValidator.builder().build().validate(createInfoRequest);
        return ok(infoToCreateInfoResponse(infoService.createInfo(createInfoRequest)));
    }

    @PutMapping
    public ResponseEntity<UpdateInfoResponse> updateInfo(@RequestBody UpdateInfoRequest updateInfoRequest) {
        UpdateInfoRequestValidator.builder().build().validate(updateInfoRequest);
        return ok(infoToUpdateInfoResponse(infoService.updateInfo(updateInfoRequest)));
    }

    @DeleteMapping
    public ResponseEntity<DeleteInfoResponse> deleteInfo(@RequestBody DeleteInfoRequest deleteInfoRequest) {
        DeleteInfoRequestValidator.builder().build().validate(deleteInfoRequest);
        infoService.deleteInfo(deleteInfoRequest);
        return ok(DeleteInfoResponse
                .builder()
                .infoName(deleteInfoRequest.getInfoName())
                .deleteMessage("Info " + deleteInfoRequest.getInfoName() + " was deleted")
                .build());
    }

    // difrractometer info
    @GetMapping("/difr/{infoName}")
    public ResponseEntity<GetDifrInfoResponse> getDifrInfo(@PathVariable String infoName) {
        NameValidator.builder().build().validate(infoName);
        return ok(difrInfoToGetDifrInfoResponse(infoService.getDifrInfo(infoName)));
    }

    @PostMapping("/difr")
    public ResponseEntity<CreateDifrInfoResponse> createDifrInfo(@RequestBody CreateDifrInfoRequest createDifrInfoRequest) {
        CreateDifrInfoRequestValidator.builder().build().validate(createDifrInfoRequest);
        return ok(difrInfoToCreateDifrInfoResponse(infoService.createDifrInfo(createDifrInfoRequest)));
    }

    @PutMapping("/difr")
    public ResponseEntity<UpdateDifrInfoResponse> updateDifrInfo(@RequestBody UpdateDifrInfoRequest updateDifrInfoRequest) {
        UpdateDifrInfoRequestValidator.builder().build().validate(updateDifrInfoRequest);
        return ok(difrInfoToUpdateDifrInfoResponse(infoService.updateDifrInfo(updateDifrInfoRequest)));
    }

    @DeleteMapping("/difr")
    public ResponseEntity<DeleteDifrInfoResponse> deleteDifrInfo(@RequestBody DeleteDifrInfoRequest deleteDifrInfoRequest) {
        DeleteDifrInfoRequestValidator.builder().build().validate(deleteDifrInfoRequest);
        infoService.deleteDifrInfo(deleteDifrInfoRequest);
        return ok(DeleteDifrInfoResponse
                .builder()
                .infoName(deleteDifrInfoRequest.getInfoName())
                .deleteMessage("Difr info " + deleteDifrInfoRequest.getInfoName() + " was deleted")
                .build());
    }

    // test info
    @GetMapping("/test/{infoName}")
    public ResponseEntity<GetTestInfoResponse> getTestInfo(@PathVariable String infoName) {
        NameValidator.builder().build().validate(infoName);
        return ok(testInfoToGetTestInfoResponse(infoService.getTestInfo(infoName)));
    }

    @PostMapping("/test")
    public ResponseEntity<CreateTestInfoResponse> createTestInfo(@RequestBody CreateTestInfoRequest createTestInfoRequest) {
        CreateTestInfoRequestValidator.builder().build().validate(createTestInfoRequest);
        return ok(testInfoToCreateTestInfoResponse(infoService.createTestInfo(createTestInfoRequest)));
    }

    @PutMapping("/test")
    public ResponseEntity<UpdateTestInfoResponse> updateTestInfo(@RequestBody UpdateTestInfoRequest updateTestInfoRequest) {
        UpdateTestInfoRequestValidator.builder().build().validate(updateTestInfoRequest);
        return ok(testInfoToUpdateTestInfoResponse(infoService.updateTestInfo(updateTestInfoRequest)));
    }

    @DeleteMapping("/test")
    public ResponseEntity<DeleteTestInfoResponse> deleteTestInfo(@RequestBody DeleteTestInfoRequest deleteTestInfoRequest) {
        infoService.deleteTestInfo(deleteTestInfoRequest);
        return ok(DeleteTestInfoResponse
                .builder()
                .infoName(deleteTestInfoRequest.getInfoName())
                .deleteMessage("Test info " + deleteTestInfoRequest.getInfoName() + " was deleted")
                .build());
    }
}
