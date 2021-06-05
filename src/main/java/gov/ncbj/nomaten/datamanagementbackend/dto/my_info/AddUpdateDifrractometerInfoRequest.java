package gov.ncbj.nomaten.datamanagementbackend.dto.my_info;

import gov.ncbj.nomaten.datamanagementbackend.model.info.subinfo.DifrractometerInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddUpdateDifrractometerInfoRequest {
    private String infoName;
    private DifrractometerInfo.Geometry geometry;
    private double incidentSoller;
    private double incidentSlit;
    private double detectorSoller;
    private double detectorSlit;
    private DifrractometerInfo.DetectorAbsorber detectorAbsorber;
    private double generatorVoltage;
    private double generatorCurrent;
    private double dataRangeStart;
    private double dataRangeEnd;
    private double stepSize;
    private double stepTime;
    private DifrractometerInfo.Stage stage;
    private boolean spinningRocking;
    private double spinningRockingVelocity;
    private double temperature;
    private String comments;
}
