package gov.ncbj.nomaten.datamanagementbackend.validators.my_data;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.ArchivePackageRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
import lombok.Builder;

@Builder
public class ArchivePackageRequestValidator implements Validator<ArchivePackageRequest> {
    @Override
    public void validate(ArchivePackageRequest archivePackageRequest) {
        notNullValidate(archivePackageRequest);
        NameValidator.builder().build().validate(archivePackageRequest.getPackageName());
    }
}
