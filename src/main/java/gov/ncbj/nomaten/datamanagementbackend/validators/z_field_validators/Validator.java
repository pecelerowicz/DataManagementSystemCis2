package gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators;

public interface Validator<T> {
    default void notNullValidate(T o) {
        if(o == null) {
            throw new RuntimeException("Argument cannot be null");
        }
    }

    void validate(T o);
}
