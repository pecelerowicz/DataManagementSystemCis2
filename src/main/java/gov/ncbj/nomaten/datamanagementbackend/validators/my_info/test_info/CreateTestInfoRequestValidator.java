package gov.ncbj.nomaten.datamanagementbackend.validators.my_info.test_info;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.testinfo.CreateTestInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import lombok.Builder;

@Builder
public class CreateTestInfoRequestValidator implements Validator<CreateTestInfoRequest> {
    @Override
    public void validate(CreateTestInfoRequest createTestInfoRequest) {
        notNullValidate(createTestInfoRequest);
        NameValidator.builder().build().validate(createTestInfoRequest.getInfoName());
        //TODO validate remaining fields
    }
}
