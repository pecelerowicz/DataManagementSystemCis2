package gov.ncbj.nomaten.datamanagementbackend.validators.my_projects;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_projects.CreateProjectRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
import lombok.Builder;

@Builder
public class CreateProjectRequestValidator implements Validator<CreateProjectRequest> {
    @Override
    public void validate(CreateProjectRequest createProjectRequest) {
        notNullValidate(createProjectRequest);
        ProjectNameValidator.builder().build().validate(createProjectRequest.getProjectName());
        ProjectDescriptionValidator.builder().build().validate(createProjectRequest.getDescription());
    }
}
