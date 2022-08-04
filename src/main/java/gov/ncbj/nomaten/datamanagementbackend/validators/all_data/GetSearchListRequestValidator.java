package gov.ncbj.nomaten.datamanagementbackend.validators.all_data;

import gov.ncbj.nomaten.datamanagementbackend.dto.all_data.GetSearchListRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.UserNameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
import lombok.Builder;

@Builder
public class GetSearchListRequestValidator implements Validator<GetSearchListRequest> {
    @Override
    public void validate(GetSearchListRequest getSearchListRequest) {
        notNullValidate(getSearchListRequest);
        String userName = getSearchListRequest.getUserName();
        UserNameValidator.builder().build().validate(userName);

        boolean hasInfo = getSearchListRequest.isHasInfo();
        boolean hasDifrInfo = getSearchListRequest.isHasDifrInfo();
        boolean hasTestInfo = getSearchListRequest.isHasTestInfo();
        if(!hasInfo) {
            if(hasDifrInfo || hasTestInfo) {
                throw new RuntimeException("Subinfo not allowed here");
            }
        }
    }
}
