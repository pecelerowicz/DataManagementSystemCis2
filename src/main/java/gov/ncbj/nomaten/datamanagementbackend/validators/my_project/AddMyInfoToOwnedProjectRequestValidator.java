package gov.ncbj.nomaten.datamanagementbackend.validators.my_project;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.AddMyInfoToOwnedProjectRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
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
