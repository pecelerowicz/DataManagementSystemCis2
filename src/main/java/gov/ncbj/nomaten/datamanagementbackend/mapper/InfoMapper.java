package gov.ncbj.nomaten.datamanagementbackend.mapper;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.InfoDto;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;

public class InfoMapper {
    public static InfoDto infoToDto(Info info) {
        return InfoDto
            .builder()
            .access(info.getAccess())
            .infoName(info.getInfoName())
            .shortName(info.getShortName())
            .longName(info.getLongName())
            .description(info.getDescription())
            .build();
    }
}
