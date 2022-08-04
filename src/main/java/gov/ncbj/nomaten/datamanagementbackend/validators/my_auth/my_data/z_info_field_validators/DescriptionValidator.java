package gov.ncbj.nomaten.datamanagementbackend.validators.my_auth.my_data.z_info_field_validators;

import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
import lombok.Builder;

@Builder
public class DescriptionValidator implements Validator<String> {
    @Override
    public void validate(String description) {
        notNullValidate(description);

        if(description.length()<1 || description.length()>1000) {
            throw new RuntimeException("Description cannot be empty or exceed 1000 characters");
        }
    }
}
