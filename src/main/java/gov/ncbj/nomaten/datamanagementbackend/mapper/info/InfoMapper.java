package gov.ncbj.nomaten.datamanagementbackend.mapper.info;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.CreateInfoResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.shared_info_project.GetInfoResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.UpdateInfoResponse;
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
            .archived(info.getArchived() != null && info.getArchived())  // TODO it should probably disappear
            .infoName(info.getInfoName())
            .title(info.getTitle())
            .shortDescription(info.getShortDescription())
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
                .title(info.getTitle())
                .shortDescription(info.getShortDescription())
                .description(info.getDescription())
                .localDate(info.getLocalDateTime().toLocalDate())
                .build();
    }

    public static UpdateInfoResponse infoToUpdateInfoResponse(Info info) {
        return UpdateInfoResponse
                .builder()
                .access(info.getAccess())
                .infoName(info.getInfoName())
                .title(info.getTitle())
                .shortDescription(info.getShortDescription())
                .description(info.getDescription())
                .localDate(info.getLocalDateTime().toLocalDate())
                .updateDifrInfoResponse(difrInfoToUpdateDifrInfoResponse(info.getDifrInfo()))
                .updateTestInfoResponse(testInfoToUpdateTestInfoResponse(info.getTestInfo()))
                .build();
    }
}
