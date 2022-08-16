package gov.ncbj.nomaten.datamanagementbackend.validators.all_projects;

import gov.ncbj.nomaten.datamanagementbackend.dto.all_projects.RemoveMyInfoFromOtherProjectRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
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
