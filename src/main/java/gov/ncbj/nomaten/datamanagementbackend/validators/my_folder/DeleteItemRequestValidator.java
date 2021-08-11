package gov.ncbj.nomaten.datamanagementbackend.validators.my_folder;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_folder.DeleteItemRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import lombok.Builder;

@Builder
public class DeleteItemRequestValidator implements Validator<DeleteItemRequest> {
    @Override
    public void validate(DeleteItemRequest deleteItemRequest) {
        notNullValidate(deleteItemRequest);
        NameValidator.builder().build().validate(deleteItemRequest.getPackageName());
        //todo improve
        String itemPathString = deleteItemRequest.getItemPathString();
        if(itemPathString == null || itemPathString.isEmpty()) {
            throw new RuntimeException("ItemPathString cannot be empty");
        }
    }
}
