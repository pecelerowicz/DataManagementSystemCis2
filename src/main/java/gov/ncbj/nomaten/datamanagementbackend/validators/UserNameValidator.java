package gov.ncbj.nomaten.datamanagementbackend.validators;

import lombok.Builder;

@Builder
public class UserNameValidator implements Validator<String> {
    @Override
    public void validate(String name) {
        notNullValidate(name);
        if(name.length()>20) {
            throw new RuntimeException("User name cannot exceed 30 characters");
        }
        if(!name.matches("[a-z]*")) {
            throw new RuntimeException("User name cannot contain white, capital, special characters or numerals");
        }
    }
}
