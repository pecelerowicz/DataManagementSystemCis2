package gov.ncbj.nomaten.datamanagementbackend.validators.my_auth.my_data;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.UploadFileRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
import lombok.Builder;

@Builder
public class UploadFileRequestValidator implements Validator<UploadFileRequest> {
    @Override
    public void validate(UploadFileRequest uploadFileRequest) {
        notNullValidate(uploadFileRequest);
        NameValidator.builder().build().validate(uploadFileRequest.getPackageName());
        // todo validate folderRelativePath
    }
}
