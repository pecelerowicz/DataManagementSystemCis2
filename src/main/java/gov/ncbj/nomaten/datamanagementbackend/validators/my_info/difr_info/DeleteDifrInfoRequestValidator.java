package gov.ncbj.nomaten.datamanagementbackend.validators.my_info.difr_info;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.difrinfo.DeleteDifrInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import gov.ncbj.nomaten.datamanagementbackend.validators.field_validators.NameValidator;
import lombok.Builder;

@Builder
public class DeleteDifrInfoRequestValidator implements Validator<DeleteDifrInfoRequest> {
    @Override
    public void validate(DeleteDifrInfoRequest deleteDifrInfoRequest) {
        notNullValidate(deleteDifrInfoRequest);
        NameValidator.builder().build().validate(deleteDifrInfoRequest.getInfoName());
    }
}
