package gov.ncbj.nomaten.datamanagementbackend.dto.my_info.difrinfo;

import gov.ncbj.nomaten.datamanagementbackend.model.info.subinfo.DifrInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDifrInfoRequest {
    private String infoName;

    private DifrInfo.Geometry geometry;
    private Double incidentSoller;
    private Double incidentSlit;
    private Double detectorSoller;
    private Double detectorSlit;
    private DifrInfo.DetectorAbsorber detectorAbsorber;
    private Double generatorVoltage;
    private Double generatorCurrent;
    private Double dataRangeStart;
    private Double dataRangeEnd;
    private Double stepSize;
    private Double stepTime;
    private DifrInfo.Stage stage;
    private Boolean spinningRocking;
    private Double spinningRockingVelocity;
    private Double temperature;
    private String comments;
}
