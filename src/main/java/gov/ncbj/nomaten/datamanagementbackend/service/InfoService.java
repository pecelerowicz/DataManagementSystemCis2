package gov.ncbj.nomaten.datamanagementbackend.service;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.AddUpdateDifrractometerInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.AddUpdateTestInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.SubInfoDto;
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
    public DifrractometerInfo addDifrractometerInfo(AddUpdateDifrractometerInfoRequest addUpdateDifrractometerInfoRequest) {
        Info info = getInfo(addUpdateDifrractometerInfoRequest.getInfoName());
        DifrractometerInfo difrractometerInfo = DifrractometerInfo
                .builder()
                .info(info)
                .geometry(addUpdateDifrractometerInfoRequest.getGeometry())
                .incidentSoller(addUpdateDifrractometerInfoRequest.getIncidentSoller())
                .incidentSlit(addUpdateDifrractometerInfoRequest.getIncidentSlit())
                .detectorSoller(addUpdateDifrractometerInfoRequest.getDetectorSoller())
                .detectorSlit(addUpdateDifrractometerInfoRequest.getDetectorSlit())
                .detectorAbsorber(addUpdateDifrractometerInfoRequest.getDetectorAbsorber())
                .generatorVoltage(addUpdateDifrractometerInfoRequest.getGeneratorVoltage())
                .generatorCurrent(addUpdateDifrractometerInfoRequest.getGeneratorCurrent())
                .dataRangeStart(addUpdateDifrractometerInfoRequest.getDataRangeStart())
                .dataRangeEnd(addUpdateDifrractometerInfoRequest.getDataRangeEnd())
                .stepSize(addUpdateDifrractometerInfoRequest.getStepSize())
                .stepTime(addUpdateDifrractometerInfoRequest.getStepTime())
                .stage(addUpdateDifrractometerInfoRequest.getStage())
                .spinningRocking(addUpdateDifrractometerInfoRequest.isSpinningRocking())
                .spinningRockingVelocity(addUpdateDifrractometerInfoRequest.getSpinningRockingVelocity())
                .temperature(addUpdateDifrractometerInfoRequest.getTemperature())
                .comments(addUpdateDifrractometerInfoRequest.getComments())
                .build();
        info.setDiffractometerInfo(difrractometerInfo);
        return difrractometerInfo;
    }

    @Transactional
    public void deleteDifrractometerInfo(SubInfoDto difrractometerInfoDto) {
        Info info = getInfo(difrractometerInfoDto.getInfoName());
        DifrractometerInfo difrractometerInfo = info.getDiffractometerInfo();
        if(difrractometerInfo == null) {
            throw new RuntimeException("No diffractometer info in: " + info.getInfoName());
        } else {
            info.setDiffractometerInfo(null);
        }
    }

    // test info
    @Transactional
    public TestInfo addTestInfo(AddUpdateTestInfoRequest addUpdateTestInfoRequest) {
        Info info = getInfo(addUpdateTestInfoRequest.getInfoName());
        TestInfo testInfo = TestInfo
                .builder()
                .info(info)
                .testField(addUpdateTestInfoRequest.getTestField())
                .build();
        info.setTestInfo(testInfo);
        return testInfo;
    }

    @Transactional
    public void deleteTestInfo(SubInfoDto testInfoDto) {
        Info info = getInfo(testInfoDto.getInfoName());
        TestInfo testInfo = info.getTestInfo();
        if (testInfo == null) {
            throw new RuntimeException("No test info in: " + info.getInfoName());
        } else {
            info.setTestInfo(null);
        }
    }

}

