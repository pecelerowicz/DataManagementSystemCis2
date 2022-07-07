package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_info.GetInfoResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_package.GetInfoListResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.*;
import gov.ncbj.nomaten.datamanagementbackend.model.PathNode;
import gov.ncbj.nomaten.datamanagementbackend.service.auxiliary.InfoService;
import gov.ncbj.nomaten.datamanagementbackend.service.main.AllProjectsService;
import gov.ncbj.nomaten.datamanagementbackend.validators.NameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.UserNameValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_project.AddMyInfoToOtherProjectRequestValidator;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_project.RemoveMyInfoFromOtherProjectRequestValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static gov.ncbj.nomaten.datamanagementbackend.mapper.info.InfoMapper.infoToGetInfoResponse;
import static gov.ncbj.nomaten.datamanagementbackend.mapper.project.ProjectMapper.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/all-projects")
public class AllProjectsController {

    private final AllProjectsService allProjectsService;
    private final InfoService infoService;

    /**
     * LEFT PANEL
     */
    @GetMapping("/project/{projectId}")
    // TODO check this method
    public ResponseEntity<GetProjectResponse> getProject(@PathVariable Long projectId) {
        // TODO idValidation
        return ok(projectToGetProjectResponse(allProjectsService.getProject(projectId)));
    }

    /**
     * LEFT PANEL
     */
    @GetMapping("/project")
    // TODO check this method
    public ResponseEntity<GetProjectsResponse> getProjects() {
        return ok(projectListToGetProjectsResponse(allProjectsService.getProjects()));
    }

    /**
     * RIGHT PANEL
     */
    @GetMapping("/info")
    public ResponseEntity<GetInfoListResponse> getInfoList() {
        return ResponseEntity.status(OK).body(new GetInfoListResponse(infoService.getInfoList()));
    }

    /**
     * RIGHT PANEL
     */
    @PostMapping("/project/info")
    public ResponseEntity<AddMyInfoToOtherProjectResponse> addMyInfoToOtherProject(@RequestBody AddMyInfoToOtherProjectRequest addMyInfoToOtherProjectRequest) {
        AddMyInfoToOtherProjectRequestValidator.builder().build().validate(addMyInfoToOtherProjectRequest);
        return ok(projectToAddMyInfoToOtherProjectResponse(allProjectsService.addMyInfoToOtherProject(addMyInfoToOtherProjectRequest)));
    }

    /**
     * RIGHT PANEL
     */
    @DeleteMapping("/project/info")
    // TODO check this method
    public ResponseEntity<RemoveMyInfoFromOtherProjectResponse> removeMyInfoFromOtherProject(@RequestBody RemoveMyInfoFromOtherProjectRequest removeMyInfoFromOtherProjectRequest) {
        RemoveMyInfoFromOtherProjectRequestValidator.builder().build().validate(removeMyInfoFromOtherProjectRequest);
        return ok(projectToRemoveMyInfoFromOtherProjectResponse(allProjectsService.removeMyInfoFromOtherProject(removeMyInfoFromOtherProjectRequest)));
    }

    /**
     * RIGHT PANEL
     */
//    @DeleteMapping("/project/all/user")
//    // TODO check this method
//    public ResponseEntity<RemoveMyFromOtherProjectResponse> removeMyFromOtherProject(@RequestBody RemoveMyFromOtherProjectRequest removeMyFromOtherProjectRequest) {
//        RemoveMyFromOtherProjectRequestValidator.builder().build().validate(removeMyFromOtherProjectRequest);
//        return ok(projectToRemoveMyFromOtherProjectResponse(projectService.removeMyFromOtherProject(removeMyFromOtherProjectRequest)));
//    }


    // TODO check what happens if I delete Info which belongs to the project (my project, other project, multiple projects)
    /**
     * RIGHT PANEL
     */
    @GetMapping("/project/packages/info/{projectId}/{userName}/{infoName}") // response type is borrowed for now
    public ResponseEntity<GetInfoResponse> getInfoOfUserAndProject(@PathVariable Long projectId, @PathVariable String userName, @PathVariable String infoName) {
        // TODO id validation (?)
        UserNameValidator.builder().build().validate(userName);
        NameValidator.builder().build().validate(infoName);
        return ok(infoToGetInfoResponse(allProjectsService.getInfoOfUserAndProject(projectId, userName, infoName)));
    }

    @GetMapping("/project/packages/folder/{projectId}/{userName}/{infoName}")
    public PathNode getPackageFolderStructureOfUserAndProject(@PathVariable Long projectId, @PathVariable String userName, @PathVariable String infoName) {
        // TODO id validation (?)
        UserNameValidator.builder().build().validate(userName);
        NameValidator.builder().build().validate(infoName);
        return allProjectsService.getPackageFolderStructureOfUserAndProject(projectId, userName, infoName);
    }
}
