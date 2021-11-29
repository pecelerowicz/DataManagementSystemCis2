package gov.ncbj.nomaten.datamanagementbackend.validators;

import lombok.Builder;

@Builder
public class PasswordValidator implements Validator<String> {
    @Override
    public void validate(String password) {
        notNullValidate(password);
        if(password.length() == 0) {
            throw new RuntimeException("Password cannot be empty");
        }
        if(password.length()<8) {
            throw new RuntimeException("Password length cannot be below 8 characters");
        }
        if(password.length()>20) {
            throw new RuntimeException("Password cannot exceed 20 characters");
        }
        if(!password.matches("[A-Za-z0-9]*")) {
            throw new RuntimeException("Password cannot contain white and special characters");
        }

        boolean hasDigit = false;
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        for(int i=0; i<password.length(); i++) {
            if(Character.isDigit(password.charAt(i))) {
                hasDigit = true;
            }
            if(Character.isUpperCase(password.charAt(i))) {
                hasUppercase = true;
            }
            if(Character.isLowerCase(password.charAt(i))) {
                hasLowercase = true;
            }
        }

        if(!hasDigit || !hasUppercase || !hasLowercase) {
            throw new RuntimeException("Password has to have lowercase, uppercase, numeral");
        }
    }
}
