package gov.ncbj.nomaten.datamanagementbackend.validators.field_validators;

import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import lombok.Builder;

@Builder
public class ShortDescriptionValidator implements Validator<String> {
    @Override
    public void validate(String shortDescription) {
        notNullValidate(shortDescription);

        if(shortDescription.length()<1 || shortDescription.length()>80) {
            throw new RuntimeException("Short description cannot be empty or exceed 80 characters");
        }
    }
}
