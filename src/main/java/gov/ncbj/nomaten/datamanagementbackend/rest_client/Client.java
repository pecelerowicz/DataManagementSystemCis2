package gov.ncbj.nomaten.datamanagementbackend.rest_client;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class Client {

    private static final RestTemplate restTemplate = new RestTemplate();
    private static String basicUrl = "http://localhost:8080/api/test";

    public static void main(String[] args) {

        ResponseEntity<String> responseEntity0 = restTemplate.getForEntity(basicUrl + "/1", String.class);
        System.out.println(responseEntity0.getBody());
        System.out.println("---");

        ResponseEntity<Dto> responseEntity1 = restTemplate.getForEntity(basicUrl + "/1", Dto.class);
        System.out.println(responseEntity1.getBody());
        System.out.println("---");

        ResponseEntity<String> responseEntity2 = restTemplate.getForEntity(basicUrl + "/list", String.class);
        System.out.println(responseEntity2.getBody());
        System.out.println("---");

        ResponseEntity<String> responseEntity3 = restTemplate.getForEntity(basicUrl + "/container", String.class);
        System.out.println(responseEntity3.getBody());
        System.out.println("---");

        ResponseEntity<DtoContainer> responseEntity4 = restTemplate.getForEntity(basicUrl + "/container", DtoContainer.class);
        System.out.println(responseEntity4.getBody());
        System.out.println("------------------");

        HttpEntity<Dto> request = new HttpEntity<>(Dto.builder().firstName("Zofia").secondName("Pecelerowicz").build());
        List response = restTemplate.postForObject(basicUrl + "/list", request, List.class);
//        response.forEach(System.out::println);
        String response2 = restTemplate.postForObject(basicUrl + "/list", request, String.class);
        System.out.println(response2);
        System.out.println("===");

    }

}
