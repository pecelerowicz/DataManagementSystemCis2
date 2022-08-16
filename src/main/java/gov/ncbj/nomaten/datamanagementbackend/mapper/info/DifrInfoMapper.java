package gov.ncbj.nomaten.datamanagementbackend.mapper.info;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.difrinfo.*;
import gov.ncbj.nomaten.datamanagementbackend.model.info.subinfo.DifrInfo;

public class DifrInfoMapper {
    public static GetDifrInfoResponse difrInfoToGetDifrInfoResponse(DifrInfo difrInfo) {
        return difrInfo == null ? null : GetDifrInfoResponse
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

    public static DifrInfo createDifrInfoRequestToDifrInfo(CreateDifrInfoRequest createDifrInfoRequest) {
        return DifrInfo.builder()
            .geometry(DifrInfo.Geometry.valueOf(createDifrInfoRequest.getGeometry()))
            .incidentSoller(createDifrInfoRequest.getIncidentSoller())
            .incidentSlit(createDifrInfoRequest.getIncidentSlit())
            .detectorSoller(createDifrInfoRequest.getDetectorSoller())
            .detectorSlit(createDifrInfoRequest.getDetectorSlit())
            .detectorAbsorber(DifrInfo.DetectorAbsorber.valueOf(createDifrInfoRequest.getDetectorAbsorber()))
            .generatorVoltage(createDifrInfoRequest.getGeneratorVoltage())
            .generatorCurrent(createDifrInfoRequest.getGeneratorCurrent())
            .dataRangeStart(createDifrInfoRequest.getDataRangeStart())
            .dataRangeEnd(createDifrInfoRequest.getDataRangeEnd())
            .stepSize(createDifrInfoRequest.getStepSize())
            .stepTime(createDifrInfoRequest.getStepTime())
            .stage(DifrInfo.Stage.valueOf(createDifrInfoRequest.getStage()))
            .spinningRocking(createDifrInfoRequest.getSpinningRocking())
            .spinningRockingVelocity(createDifrInfoRequest.getSpinningRockingVelocity())
            .temperature(createDifrInfoRequest.getTemperature())
            .comments(createDifrInfoRequest.getComments())
            .build();
    }

    public static DifrInfo updateDifrInfoRequestToDifrInfo(UpdateDifrInfoRequest updateDifrInfoRequest) {
        return DifrInfo.builder()
            .geometry(updateDifrInfoRequest.getGeometry())
            .incidentSoller(updateDifrInfoRequest.getIncidentSoller())
            .incidentSlit(updateDifrInfoRequest.getIncidentSlit())
            .detectorSoller(updateDifrInfoRequest.getDetectorSoller())
            .detectorSlit(updateDifrInfoRequest.getDetectorSlit())
            .detectorAbsorber(updateDifrInfoRequest.getDetectorAbsorber())
            .generatorVoltage(updateDifrInfoRequest.getGeneratorVoltage())
            .generatorCurrent(updateDifrInfoRequest.getGeneratorCurrent())
            .dataRangeStart(updateDifrInfoRequest.getDataRangeStart())
            .dataRangeEnd(updateDifrInfoRequest.getDataRangeEnd())
            .stepSize(updateDifrInfoRequest.getStepSize())
            .stepTime(updateDifrInfoRequest.getStepTime())
            .stage(updateDifrInfoRequest.getStage())
            .spinningRocking(updateDifrInfoRequest.isSpinningRocking())
            .spinningRockingVelocity(updateDifrInfoRequest.getSpinningRockingVelocity())
            .temperature(updateDifrInfoRequest.getTemperature())
            .comments(updateDifrInfoRequest.getComments())
            .build();
    }

    public static CreateDifrInfoResponse difrInfoToCreateDifrInfoResponse(DifrInfo difrInfo) {
        return CreateDifrInfoResponse
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

    public static UpdateDifrInfoResponse difrInfoToUpdateDifrInfoResponse(DifrInfo difrInfo) {
        return difrInfo == null ? null : UpdateDifrInfoResponse //since it is used also in InfoMapper and nulls are acceptable
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
}
