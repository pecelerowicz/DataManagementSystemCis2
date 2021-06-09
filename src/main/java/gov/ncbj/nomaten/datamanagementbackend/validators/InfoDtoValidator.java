package gov.ncbj.nomaten.datamanagementbackend.validators;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.InfoDto;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.subinfo.DifrInfoDto;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.subinfo.TestInfoDto;

//TODO check for no spaces, special characters etc.

public class InfoDtoValidator implements Validator<InfoDto> {
    @Override
    public void validate(InfoDto infoDto) {
        if(infoDto == null) {
            throw new RuntimeException("Info cannot be null");
        }

        if(infoDto.getInfoName() == null || infoDto.getInfoName().length() == 0) {
            throw new RuntimeException("Info name cannot be empty");
        }
        if(infoDto.getInfoName().length()>20) {
            throw new RuntimeException("Info name cannot exceed 20 characters");
        }
        if(infoDto.getAccess() == null) {
            throw new RuntimeException("Access cannot be empty");
        }

        String access = infoDto.getAccess().toString();
        if(!access.equals("PRIVATE") && !access.equals("PROTECTED") && !access.equals("PUBLIC")) {
            throw new RuntimeException("Access is ill defined");
        }
        if(infoDto.getAccess() != null && infoDto.getAccess().toString().length() > 20) {
            throw new RuntimeException("Access cannon exceed 20 characters");
        }

        String shortName = infoDto.getShortName();
        if(shortName != null) {
            if(shortName.length()>30) {
                throw new RuntimeException("Short name cannot exceed 30 characters");
            }
        }

        String longName = infoDto.getLongName();
        if(longName != null) {
            if(longName.length()>30) {
                throw new RuntimeException("Long name cannot exceed 50 characters");
            }
        }

        String description = infoDto.getDescription();
        if(description != null) {
            if(description.length()>500) {
                throw new RuntimeException("Description cannot exceed 500 characters");
            }
        }

        DifrInfoDto difrInfoDto = infoDto.getDifrInfoDto();
        TestInfoDto testInfoDto = infoDto.getTestInfoDto();
        int numberOfFilled = 0;
        if(difrInfoDto != null) {
            numberOfFilled++;
        }
        if(testInfoDto != null) {
            numberOfFilled++;
        }

        if(numberOfFilled > 1) {
            throw new RuntimeException("The number of subinfos cannot be greater than 1");
        }
    }
}
