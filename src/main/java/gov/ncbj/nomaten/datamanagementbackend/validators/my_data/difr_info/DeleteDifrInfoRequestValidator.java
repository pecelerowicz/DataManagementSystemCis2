package gov.ncbj.nomaten.datamanagementbackend.validators.my_data.difr_info;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_data.difrinfo.DeleteDifrInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.NameValidator;
import lombok.Builder;

@Builder
public class DeleteDifrInfoRequestValidator implements Validator<DeleteDifrInfoRequest> {
    @Override
    public void validate(DeleteDifrInfoRequest deleteDifrInfoRequest) {
        notNullValidate(deleteDifrInfoRequest);
        NameValidator.builder().build().validate(deleteDifrInfoRequest.getInfoName());
    }
}
