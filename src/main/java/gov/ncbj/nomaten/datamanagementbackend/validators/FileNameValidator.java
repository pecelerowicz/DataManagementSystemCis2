package gov.ncbj.nomaten.datamanagementbackend.validators;

import lombok.Builder;

@Builder
public class FileNameValidator implements Validator<String>{
    @Override
    public void validate(String name) {
        notNullValidate(name);

        // todo sensible validation
//        if(name.length() == 0) {
//            throw new RuntimeException("File name cannot be empty");
//        }
//        if(name.length()>30) {
//            throw new RuntimeException("File name cannot exceed 30 characters");
//        }
//        if(!name.matches("[A-Za-z]+[A-Za-z0-9]*")) {
//            throw new RuntimeException("File name cannot contain white characters, special characters or start with numeral");
//        }
    }
}
