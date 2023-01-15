package gov.ncbj.nomaten.datamanagementbackend.validators.my_data;

import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
import lombok.Builder;

@Builder
// todo should probably disappear at some point
public class IsArchivedValidator implements Validator<String> {
    @Override
    public void validate(String isArchived) {
        notNullValidate(isArchived);
        NameValidator.builder().build().validate(isArchived);
    }
}
