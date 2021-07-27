package gov.ncbj.nomaten.datamanagementbackend.validators.my_folder;

import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import lombok.Builder;

@Builder
public class DownloadFilePackageNameValidator implements Validator<String> {
    @Override
    public void validate(String packageName) {
        NameValidator.builder().build().validate(packageName);
    }
}
