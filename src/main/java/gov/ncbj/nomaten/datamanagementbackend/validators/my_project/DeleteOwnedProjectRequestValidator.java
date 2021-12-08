package gov.ncbj.nomaten.datamanagementbackend.validators.my_project;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.DeleteOwnedProjectRequest;
import gov.ncbj.nomaten.datamanagementbackend.validators.Validator;
import lombok.Builder;

@Builder
public class DeleteOwnedProjectRequestValidator implements Validator<DeleteOwnedProjectRequest> {
    @Override
    public void validate(DeleteOwnedProjectRequest deleteOwnedProjectRequest) {
        notNullValidate(deleteOwnedProjectRequest);
        // TODO validate id (?)
    }
}
