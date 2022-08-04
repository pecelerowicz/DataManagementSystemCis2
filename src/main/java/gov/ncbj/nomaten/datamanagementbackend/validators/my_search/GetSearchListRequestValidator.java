package gov.ncbj.nomaten.datamanagementbackend.validators.my_search;

import gov.ncbj.nomaten.datamanagementbackend.dto.all_data.GetSearchListRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.UserNameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
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
