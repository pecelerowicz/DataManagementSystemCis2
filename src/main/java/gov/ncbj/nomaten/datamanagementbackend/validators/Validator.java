package gov.ncbj.nomaten.datamanagementbackend.validators;

public interface Validator<T> {
    public void validate(T o);
}
