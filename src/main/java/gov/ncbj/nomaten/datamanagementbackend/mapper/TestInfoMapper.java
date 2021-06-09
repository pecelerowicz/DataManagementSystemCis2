package gov.ncbj.nomaten.datamanagementbackend.mapper;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.subinfo.TestInfoDto;
import gov.ncbj.nomaten.datamanagementbackend.model.info.subinfo.TestInfo;

public class TestInfoMapper {
    public static TestInfoDto testInfoToDto(TestInfo testInfo) {
        return TestInfoDto
            .builder()
            .infoName(testInfo.getInfo().getInfoName())
            .testField1(testInfo.getTestField1())
            .testField2(testInfo.getTestField2())
            .testField3(testInfo.getTestField3())
            .testField4(testInfo.getTestField4())
            .testField5(testInfo.getTestField5())
            .build();
    }

    public static TestInfo dtoToTestInfo(TestInfoDto testInfoDto) {
        return testInfoDto == null ? null : TestInfo
            .builder()
            .testField1(testInfoDto.getTestField1())
            .testField2(testInfoDto.getTestField2())
            .testField3(testInfoDto.getTestField3())
            .testField4(testInfoDto.getTestField4())
            .testField5(testInfoDto.getTestField5())
            .build();
    }
}
