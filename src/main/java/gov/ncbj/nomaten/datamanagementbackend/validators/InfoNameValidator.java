package gov.ncbj.nomaten.datamanagementbackend.validators;

public class InfoNameValidator implements Validator<String>{
    @Override
    public void validate(String infoName) {
        if(infoName == null || infoName.length() == 0) {
            throw new RuntimeException("InfoName cannot be empty");
        }

        if(!infoName.matches("[A-Za-z]+[A-Za-z0-9]*")) {
            throw new RuntimeException("The info name cannot contain white characters, special characters or start with numeral");
        }
    }
}
