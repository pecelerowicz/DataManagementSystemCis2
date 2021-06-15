package gov.ncbj.nomaten.datamanagementbackend.validators.field_validators;

import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import lombok.Builder;

@Builder
public class DescriptionValidator implements Validator<String> {
    @Override
    public void validate(String description) {
        notNullValidate(description);

        if(description.length()>500) {
            throw new RuntimeException("Description cannot exceed 500 characters");
        }
    }
}
