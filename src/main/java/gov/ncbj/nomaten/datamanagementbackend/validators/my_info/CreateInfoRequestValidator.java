package gov.ncbj.nomaten.datamanagementbackend.validators.my_info;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.CreateInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.field_validators.*;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import lombok.Builder;

@Builder
public class CreateInfoRequestValidator implements Validator<CreateInfoRequest> {
    @Override
    public void validate(CreateInfoRequest createInfoRequest) {
        notNullValidate(createInfoRequest);
        NameValidator.builder().build().validate(createInfoRequest.getInfoName());
        AccessValidator.builder().build().validate(createInfoRequest.getAccess());
        TitleValidator.builder().build().validate(createInfoRequest.getTitle());
        ShortDescriptionValidator.builder().build().validate(createInfoRequest.getShortDescription());
        DescriptionValidator.builder().build().validate(createInfoRequest.getDescription());
    }
}
