package gov.ncbj.nomaten.datamanagementbackend.service;

import gov.ncbj.nomaten.datamanagementbackend.dto.DataSetDto;
import gov.ncbj.nomaten.datamanagementbackend.model.DataSet;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.repository.DataSetRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class DataSetService {

    private final DataSetRepository dataSetRepository;
    private final AuthService authService;

    @Transactional
    public DataSetDto save(DataSetDto dataSetDto) {
        DataSet dataSet = mapDataSetDto(dataSetDto);
        User currentUser = authService.getCurrentUser();
        dataSet.setUser(currentUser);
        currentUser.getDataSets().add(dataSet);
        DataSet save = dataSetRepository.save(dataSet);
        dataSetDto.setId(save.getId());
        return dataSetDto;
    }

    @Transactional(readOnly = true)
    public List<DataSetDto> getAll() {
        return dataSetRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<DataSetDto> getLogged() {
        return dataSetRepository
                .findByUser(authService.getCurrentUser())
                .stream()
                .map(this::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<DataSetDto> getLoggedPagination(int pageNumber, int pageSize) {
        return dataSetRepository
                .findByUser(authService.getCurrentUser(), PageRequest.of(pageNumber, pageSize))
                .stream()
                .map(this::mapToDto)
                .collect(toList());
    }


    private DataSetDto mapToDto(DataSet dataSet) {
        return DataSetDto.builder()
                .id(dataSet.getId())
                .name(dataSet.getName())
                .description(dataSet.getDescription())
                .build();
    }

    private DataSet mapDataSetDto(DataSetDto dataSetDto) {
        return DataSet.builder()
                .name(dataSetDto.getName())
                .description(dataSetDto.getDescription())
                .build();
    }

}
