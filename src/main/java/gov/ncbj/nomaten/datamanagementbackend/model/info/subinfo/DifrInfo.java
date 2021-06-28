package gov.ncbj.nomaten.datamanagementbackend.model.info.subinfo;

import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "info_difr")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DifrInfo {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(mappedBy = "difrInfo")
    private Info info;

    @Column(name = "geometry")
    @Enumerated(EnumType.STRING)
    private Geometry geometry;

    @Column(name = "incident_soller")
    private double incidentSoller;

    @Column(name = "incident_slit")
    private double incidentSlit;

    @Column(name = "detector_soller")
    private double detectorSoller;

    @Column(name = "detector_slit")
    private double detectorSlit;

    @Column(name = "detector_absorber")
    @Enumerated(EnumType.STRING)
    private DetectorAbsorber detectorAbsorber;

    @Column(name = "generator_voltage")
    private double generatorVoltage;

    @Column(name = "generator_current")
    private double generatorCurrent;

    @Column(name = "data_range_start")
    private double dataRangeStart;

    @Column(name = "data_range_end")
    private double dataRangeEnd;

    @Column(name = "step_size")
    private double stepSize;

    @Column(name = "step_time")
    private double stepTime;

    @Column(name = "stage")
    @Enumerated(EnumType.STRING)
    private Stage stage;

    @Column(name = "spinning_rocking")
    private boolean spinningRocking;

    @Column(name = "spinning_rocking_velocity")
    private double spinningRockingVelocity;

    @Column(name = "temperature")
    private double temperature;

    @Column(name = "comments")
    private String comments;

    public enum Geometry {
        BB, Pb_GM
    }

    public enum DetectorAbsorber {
        CU01, CU02, NI01
    }

    public enum Stage {
        SPINNER, HTK1200N
    }
}



