package gov.ncbj.nomaten.datamanagementbackend.validators.my_auth.my_data;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.CreateFolderRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.FolderNameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.PathValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
import lombok.Builder;

@Builder
public class CreateFolderRequestValidator implements Validator<CreateFolderRequest> {
    @Override
    public void validate(CreateFolderRequest createFolderRequest) {
        notNullValidate(createFolderRequest);
        FolderNameValidator.builder().build().validate(createFolderRequest.getNewFolderName());
        NameValidator.builder().build().validate(createFolderRequest.getPackageName());
        PathValidator.builder().build().validate(createFolderRequest.getParentFolderRelativePath());
    }
}
