package gov.ncbj.nomaten.datamanagementbackend.service.support;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.CreateInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.UpdateInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.difrinfo.CreateDifrInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.difrinfo.DeleteDifrInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.difrinfo.UpdateDifrInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.testinfo.CreateTestInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.testinfo.DeleteTestInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.testinfo.UpdateTestInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.DeleteInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.model.info.InfoComparator;
import gov.ncbj.nomaten.datamanagementbackend.model.info.subinfo.DifrInfo;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.model.info.subinfo.TestInfo;
import gov.ncbj.nomaten.datamanagementbackend.repository.InfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static gov.ncbj.nomaten.datamanagementbackend.mapper.info.DifrInfoMapper.createDifrInfoRequestToDifrInfo;
import static gov.ncbj.nomaten.datamanagementbackend.mapper.info.DifrInfoMapper.updateDifrInfoRequestToDifrInfo;
import static gov.ncbj.nomaten.datamanagementbackend.mapper.info.TestInfoMapper.createTestInfoRequestToTestInfo;
import static gov.ncbj.nomaten.datamanagementbackend.mapper.info.TestInfoMapper.updateTestInfoRequestToTestInfo;

@Service
public class InfoService {

    private final InfoRepository infoRepository;

    @Autowired
    public InfoService(InfoRepository infoRepository) {
        this.infoRepository = infoRepository;
    }

    // info
    public List<Info> getInfoList(User user) {
        List<Info> infoList = user.getInfoList();
        infoList.sort(new InfoComparator());
        return infoList;
    }

    public Info getInfo(String infoName, User user) {
        return user.getInfoList()
            .stream()
            .filter(i -> i.getInfoName().equals(infoName))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No Info: " + infoName));
    }

    @Transactional // to be removed (this logic (filter) should be in the main service, I think)
    public Info getInfoOfUser(User user, String infoName) {
        return user
                .getInfoList()
                .stream()
                .filter(i -> i.getInfoName().equals(infoName) && i.getAccess().equals(Info.Access.PUBLIC))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User " + user.getUsername() + " does not have public package " + infoName));
    }

    public boolean infoExists(String infoName, User user) {
        return infoRepository.findByUserUsername(user.getUsername())
                .stream()
                .anyMatch(i -> i.getInfoName().equals(infoName));
    }

    @Transactional
    public Info createInfo(CreateInfoRequest createInfoRequest, User user) {
        // TODO check for info existence
        Info info = Info
            .builder()
            .infoName(createInfoRequest.getInfoName())
            .access(createInfoRequest.getAccess())
            .title(createInfoRequest.getTitle())
            .shortDescription(createInfoRequest.getShortDescription())
            .description(createInfoRequest.getDescription())
            .localDateTime(LocalDateTime.now().plusHours(2)) // ?
            .user(user)
            .build();
        user.getInfoList().add(info);
        return info;
    }

    @Transactional
    public Info updateInfo(UpdateInfoRequest updateInfoRequest, User user) {
        Info info = getInfo(updateInfoRequest.getInfoName(), user);
        info.setAccess(updateInfoRequest.getAccess());
        info.setTitle(updateInfoRequest.getTitle());
        info.setShortDescription(updateInfoRequest.getShortDescription());
        info.setDescription(updateInfoRequest.getDescription());

        info.setDifrInfo(null);
        info.setTestInfo(null);
        if(updateInfoRequest.getCreateDifrInfoRequest() != null) {
            info.setDifrInfo(createDifrInfo(updateInfoRequest.getCreateDifrInfoRequest(), user));
        }
        if(updateInfoRequest.getCreateTestInfoRequest() != null) {
            info.setTestInfo(createTestInfo(updateInfoRequest.getCreateTestInfoRequest(), user));
        }

        return info;
    }

    @Transactional
    public void deleteInfo(String infoName, User user) {
        Info info = getInfo(infoName, user);
        user.getInfoList().remove(info);
        info.setUser(null);
    }

    public List<Info> getInfoListByUser(User user) {
        return infoRepository.findByUser(user);
    }

    public List<Info> findAll() {
        return infoRepository.findAll();
    }

    public List<Info> findByUserUsername(String username) {
        return infoRepository.findByUserUsername(username);
    }

    // difrractometer info
    public DifrInfo getDifrInfo(String infoName, User user) {
        DifrInfo difrInfo = getInfo(infoName, user).getDifrInfo();
        if(difrInfo == null) {
            throw new RuntimeException("No Difr Info in " + infoName);
        } else {
            return difrInfo;
        }
    }

    @Transactional
    public DifrInfo createDifrInfo(CreateDifrInfoRequest createDifrInfoRequest, User user) {
        Info info = getInfo(createDifrInfoRequest.getInfoName(), user);
        if(info.getDifrInfo() != null) {
            throw new RuntimeException("Difr Info already exists in: " + info.getInfoName());
        }
        DifrInfo difrInfo = createDifrInfoRequestToDifrInfo(createDifrInfoRequest);
        difrInfo.setInfo(info);
        info.setDifrInfo(difrInfo);
        return difrInfo;
    }

    @Transactional
    public DifrInfo updateDifrInfo(UpdateDifrInfoRequest updateDifrInfoRequest, User user) {
        Info info = getInfo(updateDifrInfoRequest.getInfoName(), user);
        if(info.getDifrInfo() == null) {
            throw new RuntimeException("No difr info in: " + info.getInfoName());
        }
        DifrInfo newDifrInfo = updateDifrInfoRequestToDifrInfo(updateDifrInfoRequest);
        newDifrInfo.setInfo(info);
        info.setDifrInfo(newDifrInfo);
        return newDifrInfo;
    }

    @Transactional
    public void deleteDifrInfo(DeleteDifrInfoRequest deleteDifrInfoRequest, User user) {
        Info info = getInfo(deleteDifrInfoRequest.getInfoName(), user);
        DifrInfo difrInfo = info.getDifrInfo();
        if(difrInfo == null) {
            throw new RuntimeException("No difr info in: " + info.getInfoName());
        } else {
            info.setDifrInfo(null);
            difrInfo.setInfo(null);
        }
    }

    // test info
    public TestInfo getTestInfo(String infoName, User user) {
        TestInfo testInfo = getInfo(infoName, user).getTestInfo();
        if(testInfo == null) {
            throw new RuntimeException("No Test Info in " + infoName);
        } else {
            return getInfo(infoName, user).getTestInfo();
        }
    }

    @Transactional
    public TestInfo createTestInfo(CreateTestInfoRequest createTestInfoRequest, User user) {
        Info info = getInfo(createTestInfoRequest.getInfoName(), user);
        if(info.getTestInfo() != null) {
            throw new RuntimeException("Test Info already exists");
        }
        TestInfo testInfo = createTestInfoRequestToTestInfo(createTestInfoRequest);
        testInfo.setInfo(info);
        info.setTestInfo(testInfo);
        return testInfo;
    }

    @Transactional
    public TestInfo updateTestInfo(UpdateTestInfoRequest updateTestInfoRequest, User user) {
        Info info = getInfo(updateTestInfoRequest.getInfoName(), user);
        if(info.getTestInfo() == null) {
            throw new RuntimeException("Test info does not exist");
        }
        TestInfo newTestInfo = updateTestInfoRequestToTestInfo(updateTestInfoRequest);
        newTestInfo.setInfo(info);
        info.setTestInfo(newTestInfo);
        return newTestInfo;
    }

    @Transactional
    public void deleteTestInfo(DeleteTestInfoRequest deleteTestInfoRequest, User user) {
        Info info = getInfo(deleteTestInfoRequest.getInfoName(), user);
        TestInfo testInfo = info.getTestInfo();
        if (testInfo == null) {
            throw new RuntimeException("No test info in: " + info.getInfoName());
        } else {
            info.setTestInfo(null);
            testInfo.setInfo(null);
        }
    }

}
