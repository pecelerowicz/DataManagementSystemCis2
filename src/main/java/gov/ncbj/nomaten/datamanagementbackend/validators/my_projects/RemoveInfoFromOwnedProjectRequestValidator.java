package gov.ncbj.nomaten.datamanagementbackend.validators.my_projects;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_projects.RemoveInfoFromOwnedProjectRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.UserNameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
import lombok.Builder;

@Builder
public class RemoveInfoFromOwnedProjectRequestValidator implements Validator<RemoveInfoFromOwnedProjectRequest> {
    @Override
    public void validate(RemoveInfoFromOwnedProjectRequest removeInfoFromOwnedProjectRequest) {
        notNullValidate(removeInfoFromOwnedProjectRequest);
        // TODO validate id (?)
        NameValidator.builder().build().validate(removeInfoFromOwnedProjectRequest.getInfoName());
        UserNameValidator.builder().build().validate(removeInfoFromOwnedProjectRequest.getUsername());
    }
}
