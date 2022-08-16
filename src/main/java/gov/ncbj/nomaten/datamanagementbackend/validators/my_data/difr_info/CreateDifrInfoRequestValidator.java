package gov.ncbj.nomaten.datamanagementbackend.validators.my_data.difr_info;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.difrinfo.CreateDifrInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.model.info.subinfo.DifrInfo;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.NameValidator;
import lombok.Builder;

@Builder
public class CreateDifrInfoRequestValidator implements Validator<CreateDifrInfoRequest> {
    @Override
    public void validate(CreateDifrInfoRequest createDifrInfoRequest) {
        notNullValidate(createDifrInfoRequest);
        NameValidator.builder().build().validate(createDifrInfoRequest.getInfoName());

        // geometry
        if(createDifrInfoRequest.getGeometry() == null) {
            throw new RuntimeException("Geometry cannot be null");
        }
        if(!createDifrInfoRequest.getGeometry().equals(DifrInfo.Geometry.BB.toString()) &&
           !createDifrInfoRequest.getGeometry().equals(DifrInfo.Geometry.Pb_GM.toString())) {
            throw new RuntimeException("Illegal value of geometry");
        }

        // incident soller
        if(createDifrInfoRequest.getIncidentSoller() == null) {
            throw new RuntimeException("Incident Soller cannot be null");
        }
        if(createDifrInfoRequest.getIncidentSoller()<0 || createDifrInfoRequest.getIncidentSoller()>100) {
            throw new RuntimeException("Illegal value of Incident Soller");
        }

        // incident slit
        if(createDifrInfoRequest.getIncidentSlit() == null) {
            throw new RuntimeException("Incident Slit cannot be null");
        }
        if(createDifrInfoRequest.getIncidentSlit()<0 || createDifrInfoRequest.getIncidentSlit()>50) {
            throw new RuntimeException("Illegal value of Incident Slit");
        }

        // detector soller
        if(createDifrInfoRequest.getDetectorSoller() == null) {
            throw new RuntimeException("Detector Soller cannot be null");
        }
        if(createDifrInfoRequest.getDetectorSoller()<0 || createDifrInfoRequest.getDetectorSoller()>50) {
            throw new RuntimeException("Illegal value of Detector Soller");
        }

        // detector slit
        if(createDifrInfoRequest.getDetectorSlit() == null) {
            throw new RuntimeException("Detector Slit cannot be null");
        }
        if(createDifrInfoRequest.getDetectorSlit()<0 || createDifrInfoRequest.getDetectorSlit()>50) {
            throw new RuntimeException("Illegal value of Detector Slit");
        }

        // detector absorber
        if(createDifrInfoRequest.getDetectorAbsorber() == null) {
            throw new RuntimeException("Detector Absorber cannot be null");
        }
        if(!createDifrInfoRequest.getDetectorAbsorber().equals(DifrInfo.DetectorAbsorber.CU01.toString()) &&
           !createDifrInfoRequest.getDetectorAbsorber().equals(DifrInfo.DetectorAbsorber.CU02.toString()) &&
           !createDifrInfoRequest.getDetectorAbsorber().equals(DifrInfo.DetectorAbsorber.NI01.toString())) {
            throw new RuntimeException("Illegal value of Detector Absorber");
        }

        // generator voltage
        if(createDifrInfoRequest.getGeneratorVoltage() == null) {
            throw new RuntimeException("Generator Voltage cannot be null");
        }
        if(createDifrInfoRequest.getGeneratorVoltage()<0 || createDifrInfoRequest.getGeneratorVoltage()>100) {
            throw new RuntimeException("Illegal value of Generator Voltage");
        }

        // generator current
        if(createDifrInfoRequest.getGeneratorCurrent() == null) {
            throw new RuntimeException("Generator Current cannot be null");
        }
        if(createDifrInfoRequest.getGeneratorCurrent()<0 || createDifrInfoRequest.getGeneratorCurrent()>100) {
            throw new RuntimeException("Illegal value of Generator Current");
        }

        // data range start
        if(createDifrInfoRequest.getDataRangeStart() == null) {
            throw new RuntimeException("Data Range Start cannot be null");
        }
        if(createDifrInfoRequest.getDataRangeStart()<-180 || createDifrInfoRequest.getDataRangeStart()>180) {
            throw new RuntimeException("Illegal value of Data Range Start");
        }

        // data range end
        if(createDifrInfoRequest.getDataRangeEnd() == null) {
            throw new RuntimeException("Data Range End cannot be null");
        }
        if(createDifrInfoRequest.getDataRangeEnd()<-180 || createDifrInfoRequest.getDataRangeEnd()>180) {
            throw new RuntimeException("Illegal value of Data Range End");
        }

        // step size
        if(createDifrInfoRequest.getStepSize() == null) {
            throw new RuntimeException("Step Size cannot be null");
        }
        if(createDifrInfoRequest.getStepSize()<0 || createDifrInfoRequest.getStepSize()>1000) {
            throw new RuntimeException("Illegal value of Step Size");
        }

        // step time
        if(createDifrInfoRequest.getStepTime() == null) {
            throw new RuntimeException("Step Time cannot be null");
        }
        if(createDifrInfoRequest.getStepTime()<0 || createDifrInfoRequest.getStepTime()>1000000) {
            throw new RuntimeException("Illegal value of Step Time");
        }

        // stage
        if(createDifrInfoRequest.getStage() == null) {
            throw new RuntimeException("Stage cannot be null");
        }
        if(!createDifrInfoRequest.getStage().equals(DifrInfo.Stage.SPINNER.toString()) &&
           !createDifrInfoRequest.getStage().equals(DifrInfo.Stage.HTK1200N.toString())) {
            throw new RuntimeException("Illegal value of Stage");
        }

        // spinning rocking
        if(createDifrInfoRequest.getSpinningRocking() == null) {
            throw new RuntimeException("Spinning Rocking cannot be null");
        }

        // spinning rocking velocity
        if(createDifrInfoRequest.getSpinningRockingVelocity() == null) {
            throw new RuntimeException("Spinning Rocking Velocity cannot be null");
        }

        // temperature
        if(createDifrInfoRequest.getTemperature() == null) {
            throw new RuntimeException("Temperature cannot be null");
        }
        if(createDifrInfoRequest.getTemperature()<-300 || createDifrInfoRequest.getTemperature() > 5000) {
            throw new RuntimeException("Illegal value of temperature");
        }

        // comments
        if(createDifrInfoRequest.getComments() == null) {
            throw new RuntimeException("Comments cannot be null");
        }
    }
}
