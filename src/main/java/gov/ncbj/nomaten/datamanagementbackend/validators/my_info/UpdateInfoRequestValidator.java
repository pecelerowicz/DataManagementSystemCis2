package gov.ncbj.nomaten.datamanagementbackend.validators.my_info;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.UpdateInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.difrinfo.CreateDifrInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.difrinfo.UpdateDifrInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.testinfo.CreateTestInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.testinfo.UpdateTestInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import gov.ncbj.nomaten.datamanagementbackend.validators.field_validators.*;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_info.difr_info.CreateDifrInfoRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_info.difr_info.UpdateDifrInfoRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_info.test_info.CreateTestInfoRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_info.test_info.UpdateTestInfoRequestValidator;
import lombok.Builder;

@Builder
public class UpdateInfoRequestValidator implements Validator<UpdateInfoRequest> {
    @Override
    public void validate(UpdateInfoRequest updateInfoRequest) {
        notNullValidate(updateInfoRequest);
        NameValidator.builder().build().validate(updateInfoRequest.getInfoName());
        AccessValidator.builder().build().validate(updateInfoRequest.getAccess());
        ShortNameValidator.builder().build().validate(updateInfoRequest.getShortName());
        LongNameValidator.builder().build().validate(updateInfoRequest.getLongName());
        DescriptionValidator.builder().build().validate(updateInfoRequest.getDescription());

        int numberOfSubinfos = 0;
        CreateDifrInfoRequest createDifrInfoRequest = updateInfoRequest.getCreateDifrInfoRequest();
        if(createDifrInfoRequest != null) {
            CreateDifrInfoRequestValidator.builder().build().validate(createDifrInfoRequest);
            if(!createDifrInfoRequest.getInfoName().equals(updateInfoRequest.getInfoName())) {
                throw new RuntimeException("Inconsistent info names");
            }
            numberOfSubinfos++;
        }
        CreateTestInfoRequest createTestInfoRequest = updateInfoRequest.getCreateTestInfoRequest();
        if(createTestInfoRequest != null) {
            CreateTestInfoRequestValidator.builder().build().validate(createTestInfoRequest);
            if(!createTestInfoRequest.getInfoName().equals(updateInfoRequest.getInfoName())) {
                throw new RuntimeException("Inconsistent info names");
            }
            numberOfSubinfos++;
        }

        if(numberOfSubinfos > 1) {
            throw new RuntimeException("The number of subinfos cannot be greater than 1");
        }
    }
}