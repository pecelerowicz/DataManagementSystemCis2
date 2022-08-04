package gov.ncbj.nomaten.datamanagementbackend.validators.my_projects;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_projects.AddMyInfoToOwnedProjectRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
import lombok.Builder;

@Builder
public class AddMyInfoToOwnedProjectRequestValidator implements Validator<AddMyInfoToOwnedProjectRequest> {
    @Override
    public void validate(AddMyInfoToOwnedProjectRequest addMyInfoToOwnedProjectRequest) {
        notNullValidate(addMyInfoToOwnedProjectRequest);
        // TODO projectId (?)
        NameValidator.builder().build().validate(addMyInfoToOwnedProjectRequest.getInfoName());
    }
}
