package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_search.GetSearchListRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_search.GetSearchListResponse;
import gov.ncbj.nomaten.datamanagementbackend.service.SearchService;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_search.GetSearchListRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin
@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    SearchService searchService;

    @PostMapping // POST just to be able to send JSON. No content creation here!
    public GetSearchListResponse getSearchList(@RequestBody GetSearchListRequest getSearchListRequest) {
        GetSearchListRequestValidator.builder().build().validate(getSearchListRequest);
        return new GetSearchListResponse(searchService.getSearchList(getSearchListRequest));
    }

    // TODO it might be better not to hardcode it ...
    @GetMapping("/types")
    public ResponseEntity<List<String>> getTypeList() {
        return ok(Arrays.asList("General", "Difrractometer", "Test"));
    }

}
