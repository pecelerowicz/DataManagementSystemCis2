package gov.ncbj.nomaten.datamanagementbackend.validators;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_storage.CreateMetadataRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_storage.UpdatePackageRequest;

public class UpdatePackageRequestValidator implements Validator<UpdatePackageRequest> {
    @Override
    public void validate(UpdatePackageRequest updatePackageRequest) {
        if(updatePackageRequest == null ||
           updatePackageRequest.getOldName() == null ||
           updatePackageRequest.getOldName().length() == 0 ||
           updatePackageRequest.getNewName() == null ||
           updatePackageRequest.getNewName().length() == 0) {
            throw new RuntimeException("The Update Package Request cannot be empty nor contain empty fields");
        }

        if(updatePackageRequest.getNewName().length() > 20 || updatePackageRequest.getOldName().length() > 20) {
            throw new RuntimeException("The Update Package Request fields cannot exceed 20 characters");
        }

        if(!updatePackageRequest.getOldName().matches("[A-Za-z]+[A-Za-z0-9]*") ||
           !updatePackageRequest.getNewName().matches("[A-Za-z]+[A-Za-z0-9]*")) {
            throw new RuntimeException("The Update Package Request fields cannot contain white characters, special characters or start with numeral");
        }
    }
}
