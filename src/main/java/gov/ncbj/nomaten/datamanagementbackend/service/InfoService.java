package gov.ncbj.nomaten.datamanagementbackend.service;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.subinfo.DifrInfoDto;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.subinfo.TestInfoDto;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.InfoDto;
import gov.ncbj.nomaten.datamanagementbackend.model.info.subinfo.DifrInfo;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.model.info.subinfo.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static gov.ncbj.nomaten.datamanagementbackend.mapper.DifrInfoMapper.dtoToDifrInfo;
import static gov.ncbj.nomaten.datamanagementbackend.mapper.TestInfoMapper.dtoToTestInfo;

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
    public Info createInfo(InfoDto infoDto) {
        User user = authService.getCurrentUser();
        Info info = Info
            .builder()
            .infoName(infoDto.getInfoName())
            .access(infoDto.getAccess())
            .shortName(infoDto.getShortName())
            .longName(infoDto.getLongName())
            .description(infoDto.getDescription())
            .user(user)
            .build();
        user.getInfoList().add(info);
        return info;
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

        TestInfo testInfo = dtoToTestInfo(infoDto.getTestInfoDto());
        if(testInfo != null) {
            testInfo.setInfo(info);
        }
        info.setTestInfo(testInfo);

        DifrInfo difrInfo = dtoToDifrInfo(infoDto.getDifrInfoDto());
        if(difrInfo != null) {
            difrInfo.setInfo(info);
        }
        info.setDifrInfo(difrInfo);

        return info;
    }

    // difrractometer info
    @Transactional
    public DifrInfo addDifrractometerInfo(DifrInfoDto difrInfoDto) {
        Info info = getInfo(difrInfoDto.getInfoName());
        DifrInfo difrInfo = dtoToDifrInfo(difrInfoDto);
        difrInfo.setInfo(info);
        info.setDifrInfo(difrInfo);
        return difrInfo;
    }

    @Transactional
    public void deleteDifrractometerInfo(DifrInfoDto difrInfoDto) {
        Info info = getInfo(difrInfoDto.getInfoName());
        DifrInfo difrInfo = info.getDifrInfo();
        if(difrInfo == null) {
            throw new RuntimeException("No diffractometer info in: " + info.getInfoName());
        } else {
            info.setDifrInfo(null);
        }
    }

    // test info
    @Transactional
    public TestInfo addTestInfo(TestInfoDto testInfoDto) {
        Info info = getInfo(testInfoDto.getInfoName());
        TestInfo testInfo = dtoToTestInfo(testInfoDto);
        testInfo.setInfo(info);
        info.setTestInfo(testInfo);
        return testInfo;
    }

    @Transactional
    public void deleteTestInfo(TestInfoDto testInfoDto) {
        Info info = getInfo(testInfoDto.getInfoName());
        TestInfo testInfo = info.getTestInfo();
        if (testInfo == null) {
            throw new RuntimeException("No test info in: " + info.getInfoName());
        } else {
            info.setTestInfo(null);
        }
    }

}

