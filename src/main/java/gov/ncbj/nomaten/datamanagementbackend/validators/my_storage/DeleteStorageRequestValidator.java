package gov.ncbj.nomaten.datamanagementbackend.validators.my_storage;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.DeleteStorageRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import lombok.Builder;

@Builder
public class DeleteStorageRequestValidator implements Validator<DeleteStorageRequest> {
    @Override
    public void validate(DeleteStorageRequest deleteStorageRequest) {
        if(deleteStorageRequest == null || deleteStorageRequest.getStorageName() == null || deleteStorageRequest.getStorageName().length() == 0) {
            throw new RuntimeException("The Delete Storage Request cannot be empty");
        }

        if(deleteStorageRequest != null && deleteStorageRequest.getStorageName().length() > 20) {
            throw new RuntimeException("The storage name cannot exceed 20 characters");
        }

        if(!deleteStorageRequest.getStorageName().matches("[A-Za-z]+[A-Za-z0-9]*")) {
            throw new RuntimeException("The storage name cannot contain white spaces, special characters or start with numeral");
        }
    }
}
