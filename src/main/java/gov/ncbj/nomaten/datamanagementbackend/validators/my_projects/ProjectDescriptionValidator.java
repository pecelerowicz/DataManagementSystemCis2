package gov.ncbj.nomaten.datamanagementbackend.validators.my_projects;

import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
import lombok.Builder;

@Builder
public class ProjectDescriptionValidator implements Validator<String> {
    @Override
    public void validate(String projectDescription) {
        notNullValidate(projectDescription);
        if(projectDescription.length() == 0) {
            throw new RuntimeException("Project description cannot be empty");
        }
        if(projectDescription.length()>1000) {
            throw new RuntimeException("Project description cannot exceed 1000 characters");
        }
    }
}
