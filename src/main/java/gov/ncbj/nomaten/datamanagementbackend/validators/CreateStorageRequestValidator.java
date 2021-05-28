package gov.ncbj.nomaten.datamanagementbackend.validators;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_storage.CreateStorageRequest;

public class CreateStorageRequestValidator implements Validator<CreateStorageRequest> {
    @Override
    public void validate(CreateStorageRequest createStorageRequest) {
        if(createStorageRequest == null || createStorageRequest.getStorageName() == null || createStorageRequest.getStorageName().length() == 0) {
            throw new RuntimeException("The Create Storage Request cannot be empty");
        }

        if(createStorageRequest != null && createStorageRequest.getStorageName().length() > 20) {
            throw new RuntimeException("The storage name cannot exceed 20 characters");
        }

        if(!createStorageRequest.getStorageName().matches("[A-Za-z]+[A-Za-z0-9]*")) {
            throw new RuntimeException("The storage name cannot contain white characters, special characters or start with numeral");
        }
    }
}
