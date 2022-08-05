package gov.ncbj.nomaten.datamanagementbackend.validators.my_data;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.CreateStorageRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.NameValidator;
import lombok.Builder;

@Builder
public class CreateStorageRequestValidator implements Validator<CreateStorageRequest> {
    @Override
    public void validate(CreateStorageRequest createStorageRequest) {
        notNullValidate(createStorageRequest);
        NameValidator.builder().build().validate(createStorageRequest.getStorageName());
    }
}
