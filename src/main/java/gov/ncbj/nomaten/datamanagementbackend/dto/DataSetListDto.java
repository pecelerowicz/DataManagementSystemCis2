package gov.ncbj.nomaten.datamanagementbackend.dto;

import java.util.LinkedList;
import java.util.List;

public class DataSetListDto {

    private List<DataSetDto> dataSetDtoList = new LinkedList();
    private int total_count;

    public DataSetListDto() {
    }

    public DataSetListDto(List<DataSetDto> dataSetDtoList, int total_count) {
        this.dataSetDtoList = dataSetDtoList;
        this.total_count = total_count;
    }

    public List<DataSetDto> getDataSetDtoList() {
        return dataSetDtoList;
    }

    public void setDataSetDtoList(List<DataSetDto> dataSetDtoList) {
        this.dataSetDtoList = dataSetDtoList;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }
}
