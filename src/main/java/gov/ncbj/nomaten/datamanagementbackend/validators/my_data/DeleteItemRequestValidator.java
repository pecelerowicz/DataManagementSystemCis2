package gov.ncbj.nomaten.datamanagementbackend.validators.my_data;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.DeleteItemRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
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
