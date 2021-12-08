package gov.ncbj.nomaten.datamanagementbackend.validators.my_project;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.AddUserRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.UserNameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
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
