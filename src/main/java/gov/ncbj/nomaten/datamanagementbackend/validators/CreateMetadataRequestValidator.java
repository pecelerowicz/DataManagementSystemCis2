package gov.ncbj.nomaten.datamanagementbackend.validators;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_storage.CreateMetadataRequest;

public class CreateMetadataRequestValidator implements Validator<CreateMetadataRequest> {
    @Override
    public void validate(CreateMetadataRequest createMetadataRequest) {
        if(createMetadataRequest == null || createMetadataRequest.getMetadataName() == null || createMetadataRequest.getMetadataName().length() == 0) {
            throw new RuntimeException("The Create Metadata Request cannot be empty");
        }

        if(createMetadataRequest != null && createMetadataRequest.getMetadataName().length() > 20) {
            throw new RuntimeException("The metadata name cannot exceed 20 characters");
        }

        if(!createMetadataRequest.getMetadataName().matches("[A-Za-z]+[A-Za-z0-9]*")) {
            throw new RuntimeException("Metadata name cannot contain white characters, special characters or start with numeral");
        }
    }
}
