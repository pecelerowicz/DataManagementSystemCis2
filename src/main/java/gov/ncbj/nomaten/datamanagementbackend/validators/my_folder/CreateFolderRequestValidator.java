package gov.ncbj.nomaten.datamanagementbackend.validators.my_folder;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_folder.CreateFolderRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.FolderNameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.PathValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
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
