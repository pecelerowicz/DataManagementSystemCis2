package gov.ncbj.nomaten.datamanagementbackend.validators.my_auth.my_data.z_info_field_validators;

import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
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
