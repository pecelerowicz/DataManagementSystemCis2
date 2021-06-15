package gov.ncbj.nomaten.datamanagementbackend.mapper;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.CreateInfoResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.GetInfoResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.UpdateInfoResponse;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;

import static gov.ncbj.nomaten.datamanagementbackend.mapper.DifrInfoMapper.difrInfoToGetDifrInfoResponse;
import static gov.ncbj.nomaten.datamanagementbackend.mapper.TestInfoMapper.testInfoToGetTestInfoResponse;

public class InfoMapper {
    public static GetInfoResponse infoToGetInfoResponse(Info info) {
        return GetInfoResponse
            .builder()
            .access(info.getAccess())
            .infoName(info.getInfoName())
            .shortName(info.getShortName())
            .longName(info.getLongName())
            .description(info.getDescription())
            .getDifrInfoResponse(difrInfoToGetDifrInfoResponse(info.getDifrInfo()))
            .getTestInfoResponse(info.getTestInfo() == null ? null : testInfoToGetTestInfoResponse(info.getTestInfo()))
            .build();
    }

    public static CreateInfoResponse infoToCreateInfoResponse(Info info) {
        return CreateInfoResponse
                .builder()
                .access(info.getAccess())
                .infoName(info.getInfoName())
                .shortName(info.getShortName())
                .longName(info.getLongName())
                .description(info.getDescription())
                .build();
    }

    public static UpdateInfoResponse infoToUpdateInfoResponse(Info info) {
        return UpdateInfoResponse
                .builder()
                .access(info.getAccess())
                .infoName(info.getInfoName())
                .shortName(info.getShortName())
                .longName(info.getLongName())
                .description(info.getDescription())
                .build();
    }
}
