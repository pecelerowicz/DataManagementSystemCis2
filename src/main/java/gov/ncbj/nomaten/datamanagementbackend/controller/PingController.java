package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.PingDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/api/ping")
@AllArgsConstructor
@Slf4j
@CrossOrigin
public class PingController {

    @PostMapping
    public ResponseEntity<String> postPing(@RequestBody PingDto pingDto) {
        System.out.println("--- Post Ping ---");
        System.out.println(pingDto.getId());
        System.out.println(pingDto.getName());
        System.out.println(pingDto.getDescription());
        System.out.println(pingDto.getNumber());
        System.out.println("--- End of Post Ping ---");
        return ResponseEntity.status(CREATED).body("Ping received");
    }

    @GetMapping
    public ResponseEntity<PingData> getPing() {
        String[] values = {"ping 1", "ping 2", "ping 3", "ping 4"};
        return ResponseEntity.status(OK).body(new PingData(values));
    }

    @GetMapping("/table?sort={sort}&order={order}&page={page}")
    public ResponseEntity<GithubApi> getGithubApi(@PathVariable String sort, @PathVariable String order, @PathVariable String page) {
        List<GithubIssue> issueList = new LinkedList<>();
        for(int i=0; i<80; i++) {
            issueList.add(new GithubIssue(i+"", i+"", "a"+i, "b"+i));
        }

        List<GithubIssue> subList = new LinkedList<>();
        if("1".equals(page)) {
            for(int i=0; i<30; i++) {
                subList.add(issueList.get(i));
            }
        } else if("2".equals(page)) {
            for(int i=30; i<60; i++) {
                subList.add(issueList.get(i));
            }
        } else {
            for(int i=60; i<80; i++) {
                subList.add(issueList.get(i));
            }
        }

        GithubApi api = new GithubApi(subList, 80);

        return ResponseEntity.status(OK).body(api);
    }

}

class GithubApi {
    private List<GithubIssue> items = new LinkedList<>();
    private int total_count;

    public GithubApi() {
    }

    public GithubApi(List<GithubIssue> items, int total_count) {
        this.items = items;
        this.total_count = total_count;
    }

    public List<GithubIssue> getItems() {
        return items;
    }

    public void setItems(List<GithubIssue> items) {
        this.items = items;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }
}

class GithubIssue {
    private String created_at;
    private String number;
    private String state;
    private String title;

    public GithubIssue() {
    }

    public GithubIssue(String created_at, String number, String state, String title) {
        this.created_at = created_at;
        this.number = number;
        this.state = state;
        this.title = title;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

class PingData {
    private String[] values;

    public PingData() {
    }

    public PingData(String[] values) {
        this.values = values;
    }

    public String[] getValues() {
        return values;
    }

    public void setValue(String[] values) {
        this.values = values;
    }
}