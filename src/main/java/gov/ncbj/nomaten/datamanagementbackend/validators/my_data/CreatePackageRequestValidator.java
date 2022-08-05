package gov.ncbj.nomaten.datamanagementbackend.validators.my_data;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.CreatePackageRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.NameValidator;
import lombok.Builder;

@Builder
public class CreatePackageRequestValidator implements Validator<CreatePackageRequest> {
    @Override
    public void validate(CreatePackageRequest createPackageRequest) {
        notNullValidate(createPackageRequest);
        NameValidator.builder().build().validate(createPackageRequest.getPackageName());
    }
}
