package gov.ncbj.nomaten.datamanagementbackend.validators;

import lombok.Builder;

@Builder
public class FolderNameValidator implements Validator<String> {
    @Override
    public void validate(String name) {
        notNullValidate(name);
        if(name.length() == 0) {
            throw new RuntimeException("Folder name cannot be empty");
        }
        if(name.length()>20) {
            throw new RuntimeException("Folder ame cannot exceed 20 characters");
        }
        if(!name.matches("[A-Za-z]+[A-Za-z0-9]*")) {
            throw new RuntimeException("Folder name cannot contain white characters, special characters or start with numeral");
        }
    }
}
