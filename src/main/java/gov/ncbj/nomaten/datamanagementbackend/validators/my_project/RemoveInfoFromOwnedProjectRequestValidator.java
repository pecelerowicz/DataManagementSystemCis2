package gov.ncbj.nomaten.datamanagementbackend.validators.my_project;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_projects.RemoveInfoFromOwnedProjectRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.UserNameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
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
