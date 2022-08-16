package gov.ncbj.nomaten.datamanagementbackend.validators.my_data;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.CreateInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_data.z_info_field_validators.AccessValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_data.z_info_field_validators.DescriptionValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_data.z_info_field_validators.ShortDescriptionValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_data.z_info_field_validators.TitleValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
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
