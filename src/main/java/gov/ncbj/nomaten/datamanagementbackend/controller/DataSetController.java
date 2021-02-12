package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.DataSetDto;
import gov.ncbj.nomaten.datamanagementbackend.dto.DataSetListDto;
import gov.ncbj.nomaten.datamanagementbackend.service.DataSetService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@CrossOrigin
@RestController
@RequestMapping("/api/dataSet")
@AllArgsConstructor
@Slf4j
public class DataSetController {

    private final DataSetService dataSetService;

    @PostMapping
    public ResponseEntity<DataSetDto> createDataSet(@RequestBody DataSetDto dataSetDto) {
        return ResponseEntity
                .status(CREATED)
                .body(dataSetService.save(dataSetDto));
    }

    @GetMapping
    public ResponseEntity<List<DataSetDto>> getAllDataSets() {
        return ResponseEntity
                .status(OK)
                .body(dataSetService.getAll());
    }

    @GetMapping("/logged")
    public ResponseEntity<List<DataSetDto>> getLoggedDataSets() {
        return ResponseEntity
                .status(OK)
                .body(dataSetService.getLogged());
    }

    //TODO fixed total_count + rename
    @GetMapping("/logged/{pageNumber}/{pageSize}")
    public ResponseEntity<DataSetListDto> getMyDataSetPagination(@PathVariable int pageNumber, @PathVariable int pageSize) {
        return ResponseEntity
                .status(OK)
                .body(new DataSetListDto(dataSetService.getLoggedPagination(pageNumber, pageSize), 100));
    }
}
