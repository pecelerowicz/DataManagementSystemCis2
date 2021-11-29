package gov.ncbj.nomaten.datamanagementbackend.validators.field_validators;

import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import lombok.Builder;

@Builder
public class LongNameValidator implements Validator<String> {
    @Override
    public void validate(String longName) {
        notNullValidate(longName);

        if(longName.length()<1 || longName.length()>80) {
            throw new RuntimeException("Long name cannot be empty or exceed 50 characters");
        }
    }
}
