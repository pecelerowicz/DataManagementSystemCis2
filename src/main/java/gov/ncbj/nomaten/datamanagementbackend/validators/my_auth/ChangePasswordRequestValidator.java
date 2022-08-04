package gov.ncbj.nomaten.datamanagementbackend.validators.my_auth;

import gov.ncbj.nomaten.datamanagementbackend.dto.adminauth.auth.ChangePasswordRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.PasswordValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import lombok.Builder;

@Builder
public class ChangePasswordRequestValidator implements Validator<ChangePasswordRequest> {
    @Override
    public void validate(ChangePasswordRequest changePasswordRequest) {
        notNullValidate(changePasswordRequest);
        PasswordValidator.builder().build().validate(changePasswordRequest.getNewPassword());
    }
}
