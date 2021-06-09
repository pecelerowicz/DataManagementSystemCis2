package gov.ncbj.nomaten.datamanagementbackend.mapper;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.subinfo.DifrInfoDto;
import gov.ncbj.nomaten.datamanagementbackend.model.info.subinfo.DifrInfo;

public class DifrInfoMapper {
    public static DifrInfoDto difrInfoToDto(DifrInfo difrInfo) {
        return DifrInfoDto
            .builder()
            .infoName(difrInfo.getInfo().getInfoName())
            .geometry(difrInfo.getGeometry())
            .incidentSoller(difrInfo.getIncidentSoller())
            .incidentSlit(difrInfo.getIncidentSlit())
            .detectorSoller(difrInfo.getDetectorSoller())
            .detectorSlit(difrInfo.getDetectorSlit())
            .detectorAbsorber(difrInfo.getDetectorAbsorber())
            .generatorVoltage(difrInfo.getGeneratorVoltage())
            .generatorCurrent(difrInfo.getGeneratorCurrent())
            .dataRangeStart(difrInfo.getDataRangeStart())
            .dataRangeEnd(difrInfo.getDataRangeEnd())
            .stepSize(difrInfo.getStepSize())
            .stepTime(difrInfo.getStepTime())
            .stage(difrInfo.getStage())
            .spinningRocking(difrInfo.isSpinningRocking())
            .spinningRockingVelocity(difrInfo.getSpinningRockingVelocity())
            .temperature(difrInfo.getTemperature())
            .comments(difrInfo.getComments())
            .build();
    }

    public static DifrInfo dtoToDifrInfo(DifrInfoDto difrInfoDto) {
        return DifrInfo
            .builder()
            .geometry(difrInfoDto.getGeometry())
            .incidentSoller(difrInfoDto.getIncidentSoller())
            .incidentSlit(difrInfoDto.getIncidentSlit())
            .detectorSoller(difrInfoDto.getDetectorSoller())
            .detectorSlit(difrInfoDto.getDetectorSlit())
            .detectorAbsorber(difrInfoDto.getDetectorAbsorber())
            .generatorVoltage(difrInfoDto.getGeneratorVoltage())
            .generatorCurrent(difrInfoDto.getGeneratorCurrent())
            .dataRangeStart(difrInfoDto.getDataRangeStart())
            .dataRangeEnd(difrInfoDto.getDataRangeEnd())
            .stepSize(difrInfoDto.getStepSize())
            .stepTime(difrInfoDto.getStepTime())
            .stage(difrInfoDto.getStage())
            .spinningRocking(difrInfoDto.isSpinningRocking())
            .spinningRockingVelocity(difrInfoDto.getSpinningRockingVelocity())
            .temperature(difrInfoDto.getTemperature())
            .comments(difrInfoDto.getComments())
            .build();
    }
}
