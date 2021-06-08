package gov.ncbj.nomaten.datamanagementbackend.service;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.subinfo.InfoDifrRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.subinfo.InfoTestRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.InfoDto;
import gov.ncbj.nomaten.datamanagementbackend.model.info.subinfo.DifrractometerInfo;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.model.info.subinfo.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InfoService {

    private AuthService authService;

    @Autowired
    public InfoService(AuthService authService) {
        this.authService = authService;
    }

    public Info getInfo(String infoName) {
        User user = authService.getCurrentUser();
        return user.getInfoList()
                .stream()
                .filter(i -> i.getInfoName().equals(infoName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No Info: " + infoName));
    }

    @Transactional
    public Info updateInfo(InfoDto infoDto) {
        Info info = getInfo(infoDto.getInfoName());
        Info.Access access = infoDto.getAccess();
        String shortName = infoDto.getShortName();
        String longName = infoDto.getLongName();
        String description = infoDto.getDescription();

        info.setAccess(access==null || access.equals("") ? Info.Access.PRIVATE : access);
        info.setShortName(shortName==null || shortName.equals("") ? null : shortName);
        info.setLongName(longName==null || longName.equals("") ? null : longName);
        info.setDescription(description==null || description.equals("") ? null : description);

        return info;
    }

    // difrractometer info
    @Transactional
    public DifrractometerInfo addDifrractometerInfo(InfoDifrRequest infoDifrRequest) {
        Info info = getInfo(infoDifrRequest.getInfoName());
        DifrractometerInfo difrractometerInfo = DifrractometerInfo
                .builder()
                .info(info)
                .geometry(infoDifrRequest.getGeometry())
                .incidentSoller(infoDifrRequest.getIncidentSoller())
                .incidentSlit(infoDifrRequest.getIncidentSlit())
                .detectorSoller(infoDifrRequest.getDetectorSoller())
                .detectorSlit(infoDifrRequest.getDetectorSlit())
                .detectorAbsorber(infoDifrRequest.getDetectorAbsorber())
                .generatorVoltage(infoDifrRequest.getGeneratorVoltage())
                .generatorCurrent(infoDifrRequest.getGeneratorCurrent())
                .dataRangeStart(infoDifrRequest.getDataRangeStart())
                .dataRangeEnd(infoDifrRequest.getDataRangeEnd())
                .stepSize(infoDifrRequest.getStepSize())
                .stepTime(infoDifrRequest.getStepTime())
                .stage(infoDifrRequest.getStage())
                .spinningRocking(infoDifrRequest.isSpinningRocking())
                .spinningRockingVelocity(infoDifrRequest.getSpinningRockingVelocity())
                .temperature(infoDifrRequest.getTemperature())
                .comments(infoDifrRequest.getComments())
                .build();
        info.setDiffractometerInfo(difrractometerInfo);
        return difrractometerInfo;
    }

    @Transactional
    public void deleteDifrractometerInfo(InfoDifrRequest infoDifrRequest) {
        Info info = getInfo(infoDifrRequest.getInfoName());
        DifrractometerInfo difrractometerInfo = info.getDiffractometerInfo();
        if(difrractometerInfo == null) {
            throw new RuntimeException("No diffractometer info in: " + info.getInfoName());
        } else {
            info.setDiffractometerInfo(null);
        }
    }

    // test info
    @Transactional
    public TestInfo addTestInfo(InfoTestRequest infoTestRequest) {
        Info info = getInfo(infoTestRequest.getInfoName());
        TestInfo testInfo = TestInfo
                .builder()
                .info(info)
                .testField1(infoTestRequest.getTestField1())
                .testField2(infoTestRequest.getTestField2())
                .testField3(infoTestRequest.getTestField3())
                .testField4(infoTestRequest.getTestField4())
                .testField5(infoTestRequest.getTestField5())
                .build();
        info.setTestInfo(testInfo);
        return testInfo;
    }

    @Transactional
    public void deleteTestInfo(InfoTestRequest infoTestRequest) {
        Info info = getInfo(infoTestRequest.getInfoName());
        TestInfo testInfo = info.getTestInfo();
        if (testInfo == null) {
            throw new RuntimeException("No test info in: " + info.getInfoName());
        } else {
            info.setTestInfo(null);
        }
    }

}

