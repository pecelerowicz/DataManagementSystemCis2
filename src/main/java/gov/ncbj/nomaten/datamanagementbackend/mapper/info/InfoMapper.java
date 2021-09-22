package gov.ncbj.nomaten.datamanagementbackend.mapper.info;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.CreateInfoResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.GetInfoResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.UpdateInfoResponse;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;

import static gov.ncbj.nomaten.datamanagementbackend.mapper.info.DifrInfoMapper.difrInfoToGetDifrInfoResponse;
import static gov.ncbj.nomaten.datamanagementbackend.mapper.info.DifrInfoMapper.difrInfoToUpdateDifrInfoResponse;
import static gov.ncbj.nomaten.datamanagementbackend.mapper.info.TestInfoMapper.testInfoToGetTestInfoResponse;
import static gov.ncbj.nomaten.datamanagementbackend.mapper.info.TestInfoMapper.testInfoToUpdateTestInfoResponse;

public class InfoMapper {
    public static GetInfoResponse infoToGetInfoResponse(Info info) {
        return GetInfoResponse
            .builder()
            .access(info.getAccess())
            .infoName(info.getInfoName())
            .shortName(info.getShortName())
            .longName(info.getLongName())
            .description(info.getDescription())
            .localDate(info.getLocalDateTime().toLocalDate())
            .getDifrInfoResponse(difrInfoToGetDifrInfoResponse(info.getDifrInfo()))
            .getTestInfoResponse(testInfoToGetTestInfoResponse(info.getTestInfo()))
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
                .localDate(info.getLocalDateTime().toLocalDate())
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
                .localDate(info.getLocalDateTime().toLocalDate())
                .updateDifrInfoResponse(difrInfoToUpdateDifrInfoResponse(info.getDifrInfo()))
                .updateTestInfoResponse(testInfoToUpdateTestInfoResponse(info.getTestInfo()))
                .build();
    }
}
