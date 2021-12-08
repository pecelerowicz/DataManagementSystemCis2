package gov.ncbj.nomaten.datamanagementbackend.validators.my_project;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.UpdateProjectRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import lombok.Builder;

@Builder
public class UpdateProjectRequestValidator implements Validator<UpdateProjectRequest> {
    @Override
    public void validate(UpdateProjectRequest updateProjectRequest) {
        notNullValidate(updateProjectRequest);
        // TODO validate id (?)
        ProjectNameValidator.builder().build().validate(updateProjectRequest.getNewName());
        ProjectDescriptionValidator.builder().build().validate(updateProjectRequest.getNewDescription());
    }
}
