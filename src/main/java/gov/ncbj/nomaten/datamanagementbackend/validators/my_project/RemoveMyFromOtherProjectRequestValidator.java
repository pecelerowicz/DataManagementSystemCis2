package gov.ncbj.nomaten.datamanagementbackend.validators.my_project;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.RemoveMyFromOtherProjectRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import lombok.Builder;

@Builder
public class RemoveMyFromOtherProjectRequestValidator implements Validator<RemoveMyFromOtherProjectRequest> {
    @Override
    public void validate(RemoveMyFromOtherProjectRequest removeMyFromOtherProjectRequest) {
        // TODO id validation (?)
        notNullValidate(removeMyFromOtherProjectRequest);
    }
}
