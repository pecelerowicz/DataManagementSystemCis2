package gov.ncbj.nomaten.datamanagementbackend.validators.all_projects;

import gov.ncbj.nomaten.datamanagementbackend.dto.all_projects.AddMyInfoToOtherProjectRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
import lombok.Builder;

@Builder
public class AddMyInfoToOtherProjectRequestValidator implements Validator<AddMyInfoToOtherProjectRequest> {
    @Override
    public void validate(AddMyInfoToOtherProjectRequest addMyInfoToOtherProjectRequest) {
        notNullValidate(addMyInfoToOtherProjectRequest);
        // TODO idValidation (?)
        NameValidator.builder().build().validate(addMyInfoToOtherProjectRequest.getInfoName());
    }
}
