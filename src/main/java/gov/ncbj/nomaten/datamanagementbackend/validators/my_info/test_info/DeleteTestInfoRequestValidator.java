package gov.ncbj.nomaten.datamanagementbackend.validators.my_info.test_info;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.testinfo.DeleteTestInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import lombok.Builder;

@Builder
public class DeleteTestInfoRequestValidator implements Validator<DeleteTestInfoRequest> {
    @Override
    public void validate(DeleteTestInfoRequest deleteTestInfoRequest) {
        notNullValidate(deleteTestInfoRequest);
        NameValidator.builder().build().validate(deleteTestInfoRequest.getInfoName());
    }
}
