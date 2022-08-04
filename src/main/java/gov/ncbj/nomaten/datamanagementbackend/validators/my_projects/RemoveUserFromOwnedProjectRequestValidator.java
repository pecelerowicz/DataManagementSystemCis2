package gov.ncbj.nomaten.datamanagementbackend.validators.my_projects;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_projects.RemoveUserFromOwnedProjectRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.UserNameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
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
