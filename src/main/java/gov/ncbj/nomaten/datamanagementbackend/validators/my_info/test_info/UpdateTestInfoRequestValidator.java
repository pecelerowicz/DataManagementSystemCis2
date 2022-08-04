package gov.ncbj.nomaten.datamanagementbackend.validators.my_info.test_info;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.testinfo.UpdateTestInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import lombok.Builder;

@Builder
public class UpdateTestInfoRequestValidator implements Validator<UpdateTestInfoRequest> {
    @Override
    public void validate(UpdateTestInfoRequest updateTestInfoRequest) {
        notNullValidate(updateTestInfoRequest);
        NameValidator.builder().build().validate(updateTestInfoRequest.getInfoName());
        //TODO validate remaining fields
    }
}
