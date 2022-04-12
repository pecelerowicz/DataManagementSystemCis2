package gov.ncbj.nomaten.datamanagementbackend.controller.refactor;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.GetInfoResponse;
import gov.ncbj.nomaten.datamanagementbackend.service.InfoService;
import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.UserNameValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static gov.ncbj.nomaten.datamanagementbackend.mapper.info.InfoMapper.infoToGetInfoResponse;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/api/all-data/info")
public class AllDataController {

    private final InfoService infoService;

    @GetMapping("/{userName}/{infoName}") // używane w search
    public ResponseEntity<GetInfoResponse> getInfoOfUser(@PathVariable String userName, @PathVariable String infoName) {
        UserNameValidator.builder().build().validate(userName);
        NameValidator.builder().build().validate(infoName);
        return ok(infoToGetInfoResponse(infoService.getInfoOfUser(userName, infoName)));
    }


}
