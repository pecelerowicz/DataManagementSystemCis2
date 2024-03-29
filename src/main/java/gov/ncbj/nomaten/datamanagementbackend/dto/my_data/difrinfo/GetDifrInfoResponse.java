package gov.ncbj.nomaten.datamanagementbackend.dto.my_data.difrinfo;

import gov.ncbj.nomaten.datamanagementbackend.model.info.subinfo.DifrInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetDifrInfoResponse {
    private String infoName;

    private DifrInfo.Geometry geometry;
    private double incidentSoller;
    private double incidentSlit;
    private double detectorSoller;
    private double detectorSlit;
    private DifrInfo.DetectorAbsorber detectorAbsorber;
    private double generatorVoltage;
    private double generatorCurrent;
    private double dataRangeStart;
    private double dataRangeEnd;
    private double stepSize;
    private double stepTime;
    private DifrInfo.Stage stage;
    private boolean spinningRocking;
    private double spinningRockingVelocity;
    private double temperature;
    private String comments;
}
