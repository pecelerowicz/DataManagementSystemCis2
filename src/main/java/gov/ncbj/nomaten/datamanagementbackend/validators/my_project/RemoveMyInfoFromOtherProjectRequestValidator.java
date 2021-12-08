package gov.ncbj.nomaten.datamanagementbackend.validators.my_project;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.RemoveMyInfoFromOtherProjectRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import lombok.Builder;

@Builder
public class RemoveMyInfoFromOtherProjectRequestValidator implements Validator<RemoveMyInfoFromOtherProjectRequest> {
    @Override
    public void validate(RemoveMyInfoFromOtherProjectRequest removeMyInfoFromOtherProjectRequest) {
        notNullValidate(removeMyInfoFromOtherProjectRequest);
        // TODO id validation (?)
        NameValidator.builder().build().validate(removeMyInfoFromOtherProjectRequest.getInfoName());
    }
}
