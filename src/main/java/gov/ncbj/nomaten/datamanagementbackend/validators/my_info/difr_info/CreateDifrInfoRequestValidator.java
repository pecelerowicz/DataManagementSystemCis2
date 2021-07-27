package gov.ncbj.nomaten.datamanagementbackend.validators.my_info.difr_info;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.difrinfo.CreateDifrInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import lombok.Builder;

@Builder
public class CreateDifrInfoRequestValidator implements Validator<CreateDifrInfoRequest> {
    @Override
    public void validate(CreateDifrInfoRequest createDifrInfoRequest) {
        notNullValidate(createDifrInfoRequest);
        NameValidator.builder().build().validate(createDifrInfoRequest.getInfoName());
        //TODO validate remaining fields
    }
}
