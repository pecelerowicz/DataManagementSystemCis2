package gov.ncbj.nomaten.datamanagementbackend.validators.my_projects;

import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
import lombok.Builder;

@Builder
public class ProjectNameValidator implements Validator<String> {
    @Override
    public void validate(String projectName) {
        notNullValidate(projectName);
        if(projectName.length() == 0) {
            throw new RuntimeException("Project name cannot be empty");
        }
        if(projectName.length()>80) {
            throw new RuntimeException("Project name cannot exceed 80 characters");
        }
        if(!Character.isLowerCase(projectName.charAt(0)) &&
           !Character.isUpperCase(projectName.charAt(0)) &&
           !Character.isDigit(projectName.charAt(0))) {
           throw new RuntimeException("Project name cannot start with white or special character");
        }
    }
}
