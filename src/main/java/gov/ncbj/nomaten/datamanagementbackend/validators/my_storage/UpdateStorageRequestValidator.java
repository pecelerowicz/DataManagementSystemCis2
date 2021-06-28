package gov.ncbj.nomaten.datamanagementbackend.validators.my_storage;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_storage.UpdateStorageRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import gov.ncbj.nomaten.datamanagementbackend.validators.field_validators.NameValidator;
import lombok.Builder;

@Builder
public class UpdateStorageRequestValidator implements Validator<UpdateStorageRequest> {
    @Override
    public void validate(UpdateStorageRequest updateStorageRequest) {
        notNullValidate(updateStorageRequest);
        NameValidator.builder().build().validate(updateStorageRequest.getOldName());
        NameValidator.builder().build().validate(updateStorageRequest.getNewName());
        if(updateStorageRequest.getOldName().equals(updateStorageRequest.getNewName())) {
            throw new RuntimeException("Old name and new name must differ");
        }
    }
}
