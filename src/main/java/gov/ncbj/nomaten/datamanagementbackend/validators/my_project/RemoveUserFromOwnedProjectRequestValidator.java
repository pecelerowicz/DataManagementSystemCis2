package gov.ncbj.nomaten.datamanagementbackend.validators.my_project;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.RemoveUserFromOwnedProjectRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.UserNameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import lombok.Builder;

@Builder
public class RemoveUserFromOwnedProjectRequestValidator implements Validator<RemoveUserFromOwnedProjectRequest> {
    @Override
    public void validate(RemoveUserFromOwnedProjectRequest removeUserFromOwnedProjectRequest) {
        notNullValidate(removeUserFromOwnedProjectRequest);
        // TODO validate id (?)
        UserNameValidator.builder().build().validate(removeUserFromOwnedProjectRequest.getUserName());
    }
}
