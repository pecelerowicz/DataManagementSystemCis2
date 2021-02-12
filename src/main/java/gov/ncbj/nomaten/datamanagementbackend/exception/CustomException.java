package gov.ncbj.nomaten.datamanagementbackend.exception;

public class CustomException extends RuntimeException {
    public CustomException(String exMessage) {
        super(exMessage);
    }
}
