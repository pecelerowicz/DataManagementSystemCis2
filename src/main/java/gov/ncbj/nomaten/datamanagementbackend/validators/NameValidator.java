package gov.ncbj.nomaten.datamanagementbackend.validators;

import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import lombok.Builder;

@Builder
public class NameValidator implements Validator<String> {
    @Override
    public void validate(String name) {
        notNullValidate(name);
        if(name.length() == 0) {
            throw new RuntimeException("Name cannot be empty");
        }
        if(name.length()>30) {
            throw new RuntimeException("Name cannot exceed 30 characters");
        }
        if(!name.matches("[A-Za-z]+[A-Za-z0-9]*")) {
            throw new RuntimeException("Name cannot contain white characters, special characters or start with numeral");
        }
    }
}
