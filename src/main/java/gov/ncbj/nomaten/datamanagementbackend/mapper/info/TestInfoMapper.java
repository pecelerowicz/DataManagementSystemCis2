package gov.ncbj.nomaten.datamanagementbackend.mapper.info;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.testinfo.*;
import gov.ncbj.nomaten.datamanagementbackend.model.info.subinfo.TestInfo;

public class TestInfoMapper {
    public static GetTestInfoResponse testInfoToGetTestInfoResponse(TestInfo testInfo) {
        return testInfo==null ? null : GetTestInfoResponse
            .builder()
            .infoName(testInfo.getInfo().getInfoName())
            .testField1(testInfo.getTestField1())
            .testField2(testInfo.getTestField2())
            .testField3(testInfo.getTestField3())
            .testField4(testInfo.getTestField4())
            .testField5(testInfo.getTestField5())
            .build();
    }

    public static TestInfo createTestInfoRequestToTestInfo(CreateTestInfoRequest createTestInfoRequest) {
        return createTestInfoRequest == null ? null : TestInfo
                .builder()
                .testField1(createTestInfoRequest.getTestField1())
                .testField2(createTestInfoRequest.getTestField2())
                .testField3(createTestInfoRequest.getTestField3())
                .testField4(createTestInfoRequest.getTestField4())
                .testField5(createTestInfoRequest.getTestField5())
                .build();
    }

    public static TestInfo updateTestInfoRequestToTestInfo(UpdateTestInfoRequest updateTestInfoRequest) {
        return updateTestInfoRequest == null ? null : TestInfo
            .builder()
            .testField1(updateTestInfoRequest.getTestField1())
            .testField2(updateTestInfoRequest.getTestField2())
            .testField3(updateTestInfoRequest.getTestField3())
            .testField4(updateTestInfoRequest.getTestField4())
            .testField5(updateTestInfoRequest.getTestField5())
            .build();
    }

    public static CreateTestInfoResponse testInfoToCreateTestInfoResponse(TestInfo testInfo) {
        return CreateTestInfoResponse
            .builder()
            .infoName(testInfo.getInfo().getInfoName())
            .testField1(testInfo.getTestField1())
            .testField2(testInfo.getTestField2())
            .testField3(testInfo.getTestField3())
            .testField4(testInfo.getTestField4())
            .testField5(testInfo.getTestField5())
            .build();
    }

    public static UpdateTestInfoResponse testInfoToUpdateTestInfoResponse(TestInfo testInfo) {
        return testInfo == null ? null : UpdateTestInfoResponse  //since it is used also in InfoMapper and nulls are acceptable
            .builder()
            .infoName(testInfo.getInfo().getInfoName())
            .testField1(testInfo.getTestField1())
            .testField2(testInfo.getTestField2())
            .testField3(testInfo.getTestField3())
            .testField4(testInfo.getTestField4())
            .testField5(testInfo.getTestField5())
            .build();
    }
}
