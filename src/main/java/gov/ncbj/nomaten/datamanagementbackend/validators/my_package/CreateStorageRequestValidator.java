package gov.ncbj.nomaten.datamanagementbackend.validators.my_package;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_package.CreateStorageRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;

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
            throw new RuntimeException("The storage name cannot contain white spaces, special characters or start with numeral");
        }
    }
}
