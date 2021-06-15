package gov.ncbj.nomaten.datamanagementbackend.validators.my_info;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.GetInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.field_validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import lombok.Builder;

@Builder
public class GetInfoRequestValidator implements Validator<GetInfoRequest> {
    @Override
    public void validate(GetInfoRequest getInfoRequest) {
        notNullValidate(getInfoRequest);
        NameValidator.builder().build().validate(getInfoRequest.getInfoName());
    }
}
