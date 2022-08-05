package gov.ncbj.nomaten.datamanagementbackend.validators.my_data;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.DeletePackageRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.NameValidator;
import lombok.Builder;

@Builder
public class DeletePackageRequestValidator implements Validator<DeletePackageRequest> {
    @Override
    public void validate(DeletePackageRequest deletePackageRequest) {
        notNullValidate(deletePackageRequest);
        NameValidator.builder().build().validate(deletePackageRequest.getPackageName());
    }
}
