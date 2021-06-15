package gov.ncbj.nomaten.datamanagementbackend.validators;

public interface Validator<T> {
    default void notNullValidate(T o) {
        if(o == null) {
            throw new RuntimeException("Argument cannot be null");
        }
    }

    void validate(T o);
}
