package gov.ncbj.nomaten.datamanagementbackend.validators.my_package;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_package.UpdateStorageRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;

public class UpdateStorageRequestValidator implements Validator<UpdateStorageRequest> {
    @Override
    public void validate(UpdateStorageRequest updateStorageRequest) {
        if(updateStorageRequest == null ||
           updateStorageRequest.getOldName() == null ||
           updateStorageRequest.getOldName().length() == 0 ||
           updateStorageRequest.getNewName() == null ||
           updateStorageRequest.getNewName().length() == 0) {
            throw new RuntimeException("The Update Package Request cannot be empty nor contain empty fields");
        }

        if(updateStorageRequest.getNewName().length() > 20 || updateStorageRequest.getOldName().length() > 20) {
            throw new RuntimeException("The Update Package Request fields cannot exceed 20 characters");
        }

        if(!updateStorageRequest.getOldName().matches("[A-Za-z]+[A-Za-z0-9]*") ||
           !updateStorageRequest.getNewName().matches("[A-Za-z]+[A-Za-z0-9]*")) {
            throw new RuntimeException("The Update Package Request fields cannot contain white characters, special characters or start with numeral");
        }
    }
}
