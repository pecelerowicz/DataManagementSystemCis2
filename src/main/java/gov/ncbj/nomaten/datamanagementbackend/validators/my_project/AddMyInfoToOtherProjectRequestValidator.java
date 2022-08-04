package gov.ncbj.nomaten.datamanagementbackend.validators.my_project;

import gov.ncbj.nomaten.datamanagementbackend.dto.all_projects.AddMyInfoToOtherProjectRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
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
