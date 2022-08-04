package gov.ncbj.nomaten.datamanagementbackend.validators.my_projects;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_projects.DeleteOwnedProjectRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
import lombok.Builder;

@Builder
public class DeleteOwnedProjectRequestValidator implements Validator<DeleteOwnedProjectRequest> {
    @Override
    public void validate(DeleteOwnedProjectRequest deleteOwnedProjectRequest) {
        notNullValidate(deleteOwnedProjectRequest);
        // TODO validate id (?)
    }
}
