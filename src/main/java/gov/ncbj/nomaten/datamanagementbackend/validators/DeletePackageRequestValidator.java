package gov.ncbj.nomaten.datamanagementbackend.validators;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_storage.DeletePackageRequest;

public class DeletePackageRequestValidator implements Validator<DeletePackageRequest> {
    @Override
    public void validate(DeletePackageRequest deletePackageRequest) {
        if(deletePackageRequest == null || deletePackageRequest.getPackageName() == null || deletePackageRequest.getPackageName().length() == 0) {
            throw new RuntimeException("The Delete Package Request cannot be empty");
        }

        if(deletePackageRequest != null && deletePackageRequest.getPackageName().length() > 20) {
            throw new RuntimeException("The package name cannot exceed 20 characters");
        }

        if(!deletePackageRequest.getPackageName().matches("[A-Za-z]+[A-Za-z0-9]*")) {
            throw new RuntimeException("The package name cannot contain white spaces, special characters or start with numeral");
        }
    }
}
