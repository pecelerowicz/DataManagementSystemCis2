package gov.ncbj.nomaten.datamanagementbackend.service;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.CreateInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.UpdateInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.difrinfo.CreateDifrInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.difrinfo.DeleteDifrInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.difrinfo.GetDifrInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.difrinfo.UpdateDifrInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.testinfo.CreateTestInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.testinfo.DeleteTestInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.testinfo.GetTestInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.testinfo.UpdateTestInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.model.info.subinfo.DifrInfo;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.model.info.subinfo.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static gov.ncbj.nomaten.datamanagementbackend.mapper.DifrInfoMapper.createDifrInfoRequestToDifrInfo;
import static gov.ncbj.nomaten.datamanagementbackend.mapper.DifrInfoMapper.updateDifrInfoRequestToDifrInfo;
import static gov.ncbj.nomaten.datamanagementbackend.mapper.TestInfoMapper.createTestInfoRequestToTestInfo;
import static gov.ncbj.nomaten.datamanagementbackend.mapper.TestInfoMapper.updateTestInfoRequestToTestInfo;

@Service
public class InfoService {

    private AuthService authService;

    @Autowired
    public InfoService(AuthService authService) {
        this.authService = authService;
    }

    // info
    public Info getInfo(String infoName) {
        User user = authService.getCurrentUser();
        return user.getInfoList()
            .stream()
            .filter(i -> i.getInfoName().equals(infoName))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No Info: " + infoName));
    }

    @Transactional
    public Info createInfo(CreateInfoRequest createInfoRequest) {
        User user = authService.getCurrentUser();
        Info info = Info
            .builder()
            .infoName(createInfoRequest.getInfoName())
            .access(createInfoRequest.getAccess())
            .shortName(createInfoRequest.getShortName())
            .longName(createInfoRequest.getLongName())
            .description(createInfoRequest.getDescription())
            .user(user)
            .build();
        user.getInfoList().add(info);
        return info;
    }

    @Transactional
    public Info updateInfo(UpdateInfoRequest updateInfoRequest) {
        Info info = getInfo(updateInfoRequest.getInfoName());
        info.setAccess(updateInfoRequest.getAccess());
        info.setShortName(updateInfoRequest.getShortName());
        info.setLongName(updateInfoRequest.getLongName());
        info.setDescription(updateInfoRequest.getDescription());

        DifrInfo difrInfo = updateDifrInfo(updateInfoRequest.getUpdateDifrInfoRequest());
        difrInfo.setInfo(info);
        info.setDifrInfo(difrInfo);

        TestInfo testInfo = updateTestInfoRequestToTestInfo(updateInfoRequest.getUpdateTestInfoRequest());
        if(testInfo != null) {
            testInfo.setInfo(info);
        }
        info.setTestInfo(testInfo);

        return info;
    }

    // difrractometer info
    public DifrInfo getDifrInfo(GetDifrInfoRequest getDifrInfoRequest) {
        return getInfo(getDifrInfoRequest.getInfoName()).getDifrInfo();
    }

    @Transactional
    public DifrInfo createDifrInfo(CreateDifrInfoRequest createDifrInfoRequest) {
        Info info = getInfo(createDifrInfoRequest.getInfoName());
        if(info.getDifrInfo() != null) {
            throw new RuntimeException("Difr Info already created!");
        }
        DifrInfo difrInfo = createDifrInfoRequestToDifrInfo(createDifrInfoRequest);
        difrInfo.setInfo(info);
        info.setDifrInfo(difrInfo);
        return difrInfo;
    }

    @Transactional
    public DifrInfo updateDifrInfo(UpdateDifrInfoRequest updateDifrInfoRequest) {
        Info info = getInfo(updateDifrInfoRequest.getInfoName());
        if(info.getDifrInfo() == null) {
            throw new RuntimeException("Difr info does not exist");
        }
        DifrInfo newDifrInfo = updateDifrInfoRequestToDifrInfo(updateDifrInfoRequest);
        newDifrInfo.setInfo(info);
        info.setDifrInfo(newDifrInfo);
        return newDifrInfo;
    }

    @Transactional
    public void deleteDifrInfo(DeleteDifrInfoRequest deleteDifrInfoRequest) {
        Info info = getInfo(deleteDifrInfoRequest.getInfoName());
        DifrInfo difrInfo = info.getDifrInfo();
        if(difrInfo == null) {
            throw new RuntimeException("No diffractometer info in: " + info.getInfoName());
        } else {
            info.setDifrInfo(null);
            difrInfo.setInfo(null);
        }
    }

    // test info
    public TestInfo getTestInfo(GetTestInfoRequest getTestInfoRequest) {
        return getInfo(getTestInfoRequest.getInfoName()).getTestInfo();
    }

    @Transactional
    public TestInfo createTestInfo(CreateTestInfoRequest createTestInfoRequest) {
        Info info = getInfo(createTestInfoRequest.getInfoName());
        if(info.getTestInfo() != null) {
            throw new RuntimeException("Test Info already created!");
        }
        TestInfo testInfo = createTestInfoRequestToTestInfo(createTestInfoRequest);
        testInfo.setInfo(info);
        info.setTestInfo(testInfo);
        return testInfo;
    }

    @Transactional
    public TestInfo updateTestInfo(UpdateTestInfoRequest updateTestInfoRequest) {
        Info info = getInfo(updateTestInfoRequest.getInfoName());
        if(info.getTestInfo() == null) {
            throw new RuntimeException("Test info does not exist");
        }
        TestInfo newTestInfo = updateTestInfoRequestToTestInfo(updateTestInfoRequest);
        newTestInfo.setInfo(info);
        info.setTestInfo(newTestInfo);
        return newTestInfo;
    }

    @Transactional
    public void deleteTestInfo(DeleteTestInfoRequest deleteTestInfoRequest) {
        Info info = getInfo(deleteTestInfoRequest.getInfoName());
        TestInfo testInfo = info.getTestInfo();
        if (testInfo == null) {
            throw new RuntimeException("No test info in: " + info.getInfoName());
        } else {
            info.setTestInfo(null);
            testInfo.setInfo(null);
        }
    }

}

