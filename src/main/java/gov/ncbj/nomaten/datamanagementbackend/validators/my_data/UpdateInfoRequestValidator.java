package gov.ncbj.nomaten.datamanagementbackend.validators.my_data;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.UpdateInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.difrinfo.CreateDifrInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.testinfo.CreateTestInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_data.z_info_field_validators.DescriptionValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_data.z_info_field_validators.ShortDescriptionValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_data.z_info_field_validators.AccessValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_data.z_info_field_validators.TitleValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_data.difr_info.CreateDifrInfoRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_data.test_info.CreateTestInfoRequestValidator;
import lombok.Builder;

@Builder
public class UpdateInfoRequestValidator implements Validator<UpdateInfoRequest> {
    @Override
    public void validate(UpdateInfoRequest updateInfoRequest) {
        notNullValidate(updateInfoRequest);
        NameValidator.builder().build().validate(updateInfoRequest.getInfoName());
        AccessValidator.builder().build().validate(updateInfoRequest.getAccess());
        TitleValidator.builder().build().validate(updateInfoRequest.getTitle());
        ShortDescriptionValidator.builder().build().validate(updateInfoRequest.getShortDescription());
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
