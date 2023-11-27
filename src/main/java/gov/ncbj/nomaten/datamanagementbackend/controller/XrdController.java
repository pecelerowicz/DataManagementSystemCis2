package gov.ncbj.nomaten.datamanagementbackend.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/xrd")
public class XrdController {

    public ResponseEntity<String> getXrd() {
        throw new RuntimeException("Xrd not implemented yet");
    }

}
