package gov.ncbj.nomaten.datamanagementbackend.validators.my_project;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.CreateProjectRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
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
