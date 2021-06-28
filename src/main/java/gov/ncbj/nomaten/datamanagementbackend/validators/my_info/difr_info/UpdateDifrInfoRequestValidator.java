package gov.ncbj.nomaten.datamanagementbackend.validators.my_info.difr_info;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.difrinfo.UpdateDifrInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import gov.ncbj.nomaten.datamanagementbackend.validators.field_validators.NameValidator;
import lombok.Builder;

@Builder
public class UpdateDifrInfoRequestValidator implements Validator<UpdateDifrInfoRequest> {
    @Override
    public void validate(UpdateDifrInfoRequest updateDifrInfoRequest) {
        notNullValidate(updateDifrInfoRequest);
        NameValidator.builder().build().validate(updateDifrInfoRequest.getInfoName());
        //TODO validate remaining fields
    }
}
