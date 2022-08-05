package gov.ncbj.nomaten.datamanagementbackend.client;

import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/test")
public class Controller {

    private List<Dto> dtoList = new LinkedList<>();

    public Controller() {
        this.dtoList.add(Dto.builder().id("1").firstName("Michal").secondName("Pecelerowicz").build());
        this.dtoList.add(Dto.builder().id("2").firstName("Konrad").secondName("Pecelerowicz").build());
        this.dtoList.add(Dto.builder().id("3").firstName("Stanisław").secondName("Pecelerowicz").build());
    }

    @GetMapping("/{id}")
    private Dto getDto(@PathVariable String id) {
        return this.dtoList.stream().filter(d -> d.getId().equals(id)).findAny().orElseThrow(() -> new RuntimeException("No dto with id " + id));
    }

    @GetMapping("/list")
    private List<Dto> getDtos() {
        return this.dtoList;
    }

    @GetMapping("/container")
    private DtoContainer getTestInContainer() {
        return DtoContainer.builder().dtoList(this.dtoList).build();
    }

    @PostMapping("/list")
    private List<Dto> addDto(@RequestBody Dto dto) {
        int size = this.dtoList.size();
        Dto last = this.dtoList.get(size - 1);
        String lastId = last.getId();
        int id = Integer.parseInt(lastId) + 1;
        String newId = Integer.toString(id);
        Dto newDto = new Dto(newId , dto.getFirstName(), dto.getSecondName());
        this.dtoList.add(newDto);
        return this.dtoList;
    }

    @PutMapping("/list")
    private List<Dto> changeDto(@RequestBody Dto dto) {
        Dto myDto = this.dtoList.stream()
                .filter(d -> d.getId().equals(dto.getId()))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No dto with this id"));
        myDto.setFirstName(dto.getFirstName());
        myDto.setSecondName(dto.getSecondName());
        return this.dtoList;
    }

    @DeleteMapping("/list/{id}")
    private List<Dto> deleteDto(@PathVariable String id) {
        this.dtoList = this.dtoList.stream().filter(d -> !d.getId().equals(id)).collect(Collectors.toList());
        return this.dtoList;
    }

}




