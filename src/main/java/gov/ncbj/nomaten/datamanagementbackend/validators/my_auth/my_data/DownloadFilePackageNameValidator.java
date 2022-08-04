package gov.ncbj.nomaten.datamanagementbackend.validators.my_auth.my_data;

import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
import lombok.Builder;

@Builder
public class DownloadFilePackageNameValidator implements Validator<String> {
    @Override
    public void validate(String packageName) {
        NameValidator.builder().build().validate(packageName);
    }
}
