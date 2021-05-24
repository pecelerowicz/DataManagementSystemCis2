package gov.ncbj.nomaten.datamanagementbackend.service;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.DeviceDto;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.InfoDto;
import gov.ncbj.nomaten.datamanagementbackend.model.Device;
import gov.ncbj.nomaten.datamanagementbackend.model.Info;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InfoService {

    private AuthService authService;
    private DeviceRepository deviceRepository;

    @Autowired
    public InfoService(AuthService authService, DeviceRepository deviceRepository) {
        this.authService = authService;
        this.deviceRepository = deviceRepository;
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
        User user = authService.getCurrentUser();
        Info info = user.getInfoList()
                .stream()
                .filter(i -> i.getInfoName().equals(infoDto.getInfoName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No Info: " + infoDto.getInfoName()));

        Info.Access access = infoDto.getAccess();
        String shortName = infoDto.getShortName();
        String longName = infoDto.getLongName();
        DeviceDto deviceDto = infoDto.getDeviceDto();

        info.setAccess(access==null || access.equals("") ? Info.Access.PRIVATE : access);
        info.setShortName(shortName==null || shortName.equals("") ? null : shortName);
        info.setLongName(longName==null || longName.equals("") ? null : longName);
        Device device = deviceRepository
                .findByName(deviceDto.getName())
                .orElseThrow(() -> new RuntimeException("No device: " + deviceDto.getName()));
        info.setDevice(device);

        return info;
    }

}
