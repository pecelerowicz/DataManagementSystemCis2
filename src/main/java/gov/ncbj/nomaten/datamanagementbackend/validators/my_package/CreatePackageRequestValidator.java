package gov.ncbj.nomaten.datamanagementbackend.validators.my_package;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_package.CreatePackageRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import lombok.Builder;

@Builder
public class CreatePackageRequestValidator implements Validator<CreatePackageRequest> {
    @Override
    public void validate(CreatePackageRequest createPackageRequest) {
        notNullValidate(createPackageRequest);
        NameValidator.builder().build().validate(createPackageRequest.getPackageName());
    }
}
