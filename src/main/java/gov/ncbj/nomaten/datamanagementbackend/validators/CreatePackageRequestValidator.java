package gov.ncbj.nomaten.datamanagementbackend.validators;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_storage.CreatePackageRequest;

public class CreatePackageRequestValidator implements Validator<CreatePackageRequest> {
    @Override
    public void validate(CreatePackageRequest createPackageRequest) {
        if(createPackageRequest == null || createPackageRequest.getPackageName() == null || createPackageRequest.getPackageName().length() == 0) {
            throw new RuntimeException("The Create Package Request cannot be empty");
        }

        if(createPackageRequest != null && createPackageRequest.getPackageName().length() > 20) {
            throw new RuntimeException("The package name cannot exceed 20 characters");
        }

        if(!createPackageRequest.getPackageName().matches("[A-Za-z]+[A-Za-z0-9]*")) {
            throw new RuntimeException("The package name cannot contain white spaces, special characters or start with numeral");
        }
    }
}
