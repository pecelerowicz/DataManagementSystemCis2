package gov.ncbj.nomaten.datamanagementbackend.validators.my_data.z_info_field_validators;

import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
import lombok.Builder;

@Builder
public class TitleValidator implements Validator<String> {
    @Override
    public void validate(String title) {
        notNullValidate(title);

        if(title.length()<1 || title.length()>50) {
            throw new RuntimeException("Title cannot be empty or exceed 50 characters");
        }
    }
}
