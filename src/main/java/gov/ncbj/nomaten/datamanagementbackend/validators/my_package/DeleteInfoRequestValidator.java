package gov.ncbj.nomaten.datamanagementbackend.validators.my_package;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_package.DeleteInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;

public class DeleteInfoRequestValidator implements Validator<DeleteInfoRequest> {
    @Override
    public void validate(DeleteInfoRequest deleteInfoRequest) {
        if(deleteInfoRequest == null || deleteInfoRequest.getInfoName() == null || deleteInfoRequest.getInfoName().length() == 0) {
            throw new RuntimeException("The Delete Storage Request cannot be empty");
        }

        if(deleteInfoRequest != null && deleteInfoRequest.getInfoName().length() > 20) {
            throw new RuntimeException("The storage name cannot exceed 20 characters");
        }

        if(!deleteInfoRequest.getInfoName().matches("[A-Za-z]+[A-Za-z0-9]*")) {
            throw new RuntimeException("The storage name cannot contain white spaces, special characters or start with numeral");
        }
    }
}
