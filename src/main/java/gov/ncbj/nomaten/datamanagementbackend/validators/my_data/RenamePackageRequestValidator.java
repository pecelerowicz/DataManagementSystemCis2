package gov.ncbj.nomaten.datamanagementbackend.validators.my_data;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.RenamePackageRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
import lombok.Builder;

@Builder
public class RenamePackageRequestValidator implements Validator<RenamePackageRequest> {
    @Override
    public void validate(RenamePackageRequest renamePackageRequest) {
        notNullValidate(renamePackageRequest);
        NameValidator.builder().build().validate(renamePackageRequest.getPackageOldName());
        NameValidator.builder().build().validate(renamePackageRequest.getPackageNewName());
        if(renamePackageRequest.getPackageNewName().equals(renamePackageRequest.getPackageOldName())) {
            throw new RuntimeException("New and old names cannot be the same");
        }
    }
}
