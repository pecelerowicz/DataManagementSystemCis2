package gov.ncbj.nomaten.datamanagementbackend.mapper;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.InfoDto;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;

import static gov.ncbj.nomaten.datamanagementbackend.mapper.DifrInfoMapper.difrInfoToDto;
import static gov.ncbj.nomaten.datamanagementbackend.mapper.TestInfoMapper.testInfoToDto;

public class InfoMapper {
    public static InfoDto infoToDto(Info info) {
        System.out.println();
        return InfoDto
            .builder()
            .access(info.getAccess())
            .infoName(info.getInfoName())
            .shortName(info.getShortName())
            .longName(info.getLongName())
            .description(info.getDescription())
            .difrInfoDto(info.getDifrInfo() == null ? null : difrInfoToDto(info.getDifrInfo()))
            .testInfoDto(info.getTestInfo() == null ? null : testInfoToDto(info.getTestInfo()))
            .build();
    }
}
