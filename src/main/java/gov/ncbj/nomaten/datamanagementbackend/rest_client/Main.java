package gov.ncbj.nomaten.datamanagementbackend.rest_client;

import com.sun.net.httpserver.Headers;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_auth.AuthenticationResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_auth.LoginRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.CreateInfoRequest;
import gov.ncbj.nomaten.datamanagementbackend.model.info.Info;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Main {

    private static final RestTemplate restTemplate = new RestTemplate();
    private static String basicUrl = "http://localhost:8080/api";

    public static void main(String[] args) {

        String authToken1 = getAuthToken("michal", "1Aaaaaaa");
        String authToken2 = getAuthToken("konrad", "1Aaaaaaa");
        String authToken3 = getAuthToken("stanislaw", "1Aaaaaaa");
        String authToken4 = getAuthToken("maksymilian", "1Aaaaaaa");
        String authToken5 = getAuthToken("wiktor", "1Aaaaaaa");

        //System.out.println(authToken);

        for(int i=0; i<5000; i++) {
            if(i<10) {
                createInfo("michal000" + i, authToken1);
                createInfo("konrad000" + i, authToken2);
                createInfo("stanislaw000" + i, authToken3);
                createInfo("maksymilian000" + i, authToken4);
                createInfo("wiktor000" + i, authToken5);
            } else if (i<100) {
                createInfo("michal00" + i, authToken1);
                createInfo("konrad00" + i, authToken2);
                createInfo("stanislaw00" + i, authToken3);
                createInfo("maksymilian00" + i, authToken4);
                createInfo("wiktor00" + i, authToken5);
            } else if(i<1000) {
                createInfo("michal0" + i, authToken1);
                createInfo("konrad0" + i, authToken2);
                createInfo("stanislaw0" + i, authToken3);
                createInfo("maksymilian0" + i, authToken4);
                createInfo("wiktor0" + i, authToken5);
            } else {
                createInfo("michal" + i, authToken1);
                createInfo("konrad" + i, authToken2);
                createInfo("stanislaw" + i, authToken3);
                createInfo("maksymilian" + i, authToken4);
                createInfo("wiktor" + i, authToken5);
            }
        }
//        String resp = getInfoList(authToken);

//        System.out.println(resp);

    }

    private static String createInfo(String infoName, String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + token);

        CreateInfoRequest createInfoRequest = CreateInfoRequest.builder()
                .infoName(infoName)
                .access(Info.Access.PRIVATE)
                .title("title title title")
                .shortDescription("short short short short short")
                .description("012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789")
                .build();

        HttpEntity<CreateInfoRequest> request = new HttpEntity<>(createInfoRequest, httpHeaders);
        return restTemplate.exchange(basicUrl + "/info", HttpMethod.POST, request, String.class).getBody();
    }


    private static String getInfoList(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(basicUrl + "/info", HttpMethod.GET, request, String.class);
        return response.getBody();
    }

    private static String getAuthToken(String username, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);
        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest);
//        String response1 = restTemplate.postForObject(basicUrl + "/login", request, String.class);
//        System.out.println(response1);
        AuthenticationResponse response2 = restTemplate.postForObject(basicUrl + "/auth/login", request, AuthenticationResponse.class);
        return response2.getAuthenticationToken();
    }

}
