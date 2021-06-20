package gov.ncbj.nomaten.datamanagementbackend.validators.my_info;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.DeleteInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import gov.ncbj.nomaten.datamanagementbackend.validators.field_validators.NameValidator;
import lombok.Builder;

@Builder
public class DeleteInfoRequestValidator implements Validator<DeleteInfoRequest> {
    @Override
    public void validate(DeleteInfoRequest deleteInfoRequest) {
        notNullValidate(deleteInfoRequest);
        NameValidator.builder().build().validate(deleteInfoRequest.getInfoName());
    }
}
