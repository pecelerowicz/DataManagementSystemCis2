package gov.ncbj.nomaten.datamanagementbackend.validators.my_auth.my_data;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.DeleteInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.NameValidator;
import lombok.Builder;

@Builder
public class DeleteInfoRequestValidator implements Validator<DeleteInfoRequest> {
    @Override
    public void validate(DeleteInfoRequest deleteInfoRequest) {
        notNullValidate(deleteInfoRequest);
        NameValidator.builder().build().validate(deleteInfoRequest.getInfoName());
    }
}
