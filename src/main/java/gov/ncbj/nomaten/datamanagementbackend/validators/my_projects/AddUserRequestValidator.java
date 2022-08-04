package gov.ncbj.nomaten.datamanagementbackend.validators.my_projects;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_projects.AddUserRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.UserNameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
import lombok.Builder;

@Builder
public class AddUserRequestValidator implements Validator<AddUserRequest> {
    @Override
    public void validate(AddUserRequest addUserRequest) {
        notNullValidate(addUserRequest);
        // TODO validate id (?)
        UserNameValidator.builder().build().validate(addUserRequest.getUserName());
    }
}
