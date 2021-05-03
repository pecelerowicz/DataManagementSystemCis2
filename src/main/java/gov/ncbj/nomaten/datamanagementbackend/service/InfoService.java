package gov.ncbj.nomaten.datamanagementbackend.service;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.UpdateInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.model.Info;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
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
                .filter(i -> i.getName().equals(infoName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No Info: " + infoName));
    }

    @Transactional
    public Info updateInfo(UpdateInfoRequest updateInfoRequest) {
        User user = authService.getCurrentUser();
        Info info = user.getInfoList()
                .stream()
                .filter(i -> i.getName().equals(updateInfoRequest.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No Info: " + updateInfoRequest.getName()));
        Info.Access access = updateInfoRequest.getAccess();
        String shortName = updateInfoRequest.getShortName();
        String longName = updateInfoRequest.getLongName();
        info.setAccess(access==null || access.equals("") ? Info.Access.PRIVATE : access);
        info.setShortName(shortName==null || shortName.equals("") ? null : shortName);
        info.setLongName(longName==null || longName.equals("") ? null : longName);
        return info;
    }

}
