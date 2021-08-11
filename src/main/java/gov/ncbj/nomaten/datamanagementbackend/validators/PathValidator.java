package gov.ncbj.nomaten.datamanagementbackend.validators;

import lombok.Builder;

@Builder
public class PathValidator implements Validator<String> {
    @Override
    public void validate(String path) {
        notNullValidate(path);
        // todo actual path validation
    }
}
