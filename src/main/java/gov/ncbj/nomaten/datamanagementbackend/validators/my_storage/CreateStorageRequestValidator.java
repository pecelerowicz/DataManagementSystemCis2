package gov.ncbj.nomaten.datamanagementbackend.validators.my_storage;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_storage.CreateStorageRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import gov.ncbj.nomaten.datamanagementbackend.validators.field_validators.NameValidator;
import lombok.Builder;

@Builder
public class CreateStorageRequestValidator implements Validator<CreateStorageRequest> {
    @Override
    public void validate(CreateStorageRequest createStorageRequest) {
        notNullValidate(createStorageRequest);
        NameValidator.builder().build().validate(createStorageRequest.getStorageName());
    }
}
