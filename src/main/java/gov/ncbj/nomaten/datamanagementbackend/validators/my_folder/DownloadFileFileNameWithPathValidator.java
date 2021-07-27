package gov.ncbj.nomaten.datamanagementbackend.validators.my_folder;

import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import lombok.Builder;

@Builder
public class DownloadFileFileNameWithPathValidator implements Validator<String> {
    @Override
    // TODO check and possibly extend
    public void validate(String fileNameWithPath) {
        notNullValidate(fileNameWithPath);
    }
}
