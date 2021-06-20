package gov.ncbj.nomaten.datamanagementbackend.validators.my_package;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_package.DeletePackageRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import gov.ncbj.nomaten.datamanagementbackend.validators.field_validators.NameValidator;
import lombok.Builder;

@Builder
public class DeletePackageRequestValidator implements Validator<DeletePackageRequest> {
    @Override
    public void validate(DeletePackageRequest deletePackageRequest) {
        notNullValidate(deletePackageRequest);
        NameValidator.builder().build().validate(deletePackageRequest.getPackageName());
    }
}
