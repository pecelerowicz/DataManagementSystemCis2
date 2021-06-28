package gov.ncbj.nomaten.datamanagementbackend.validators.field_validators;

import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import lombok.Builder;

@Builder
public class ShortNameValidator implements Validator<String> {
    @Override
    public void validate(String shortName) {
        notNullValidate(shortName);

        if(shortName.length()>30) {
            throw new RuntimeException("Short name cannot exceed 30 characters");
        }
    }
}