package gov.ncbj.nomaten.datamanagementbackend.validators.my_data.z_info_field_validators;

import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import gov.ncbj.nomaten.datamanagementbackend.validators.z_field_validators.Validator;
import lombok.Builder;

@Builder
public class AccessValidator implements Validator<Info.Access> {
    @Override
    public void validate(Info.Access access) {
        notNullValidate(access);

        String stringAccess = access.toString();
        if(!stringAccess.equals("PRIVATE") && !stringAccess.equals("PROTECTED") && !stringAccess.equals("PUBLIC")) {
            throw new RuntimeException("Access is ill defined");
        }
    }
}
