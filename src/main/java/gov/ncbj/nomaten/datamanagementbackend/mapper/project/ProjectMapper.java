package gov.ncbj.nomaten.datamanagementbackend.mapper.project;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_project.*;
import gov.ncbj.nomaten.datamanagementbackend.model.Project;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ProjectMapper {
    public static GetProjectResponse projectToGetProjectResponse(Project project) {
        return GetProjectResponse.builder()
                .id(project.getId())
                .name(project.getProjectName())
                .description(project.getDescription())
                .localDate(project.getLocalDateTime().toLocalDate())
                .ownerName(project.getOwnerName())
                .memberNames(project.getUsers().stream().map(User::getUsername).collect(toList()))
                .projectInfoResponseList(project.getInfoList().stream().sorted((i1, i2) -> -i1.getLocalDateTime().compareTo(i2.getLocalDateTime())).map(ProjectInfoResponse::new).collect(toList()))
                .build();
    }

    public static GetProjectsResponse projectListToGetProjectsResponse(List<Project> projects) {
        return GetProjectsResponse
                .builder()
                .getProjectResponseList(projects.stream()
                        .map(ProjectMapper::projectToGetProjectResponse)
                        .collect(toList()))
                .build();
    }

    public static CreateProjectResponse projectToCreateProjectResponse(Project project) {
        return CreateProjectResponse.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                .description(project.getDescription())
                .localDate(project.getLocalDateTime().toLocalDate())
                .ownerName(project.getOwnerName())
                .build();
    }

    public static UpdateProjectResponse projectToUpdateProjectResponse(Project project) {
        return UpdateProjectResponse.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                .description(project.getDescription())
                .localDate(project.getLocalDateTime().toLocalDate())
                .ownerName(project.getOwnerName())
                .memberNames(project.getUsers().stream().map(User::getUsername).collect(toList()))
                .projectInfoResponseList(project.getInfoList().stream().sorted((i1, i2) -> -i1.getLocalDateTime().compareTo(i2.getLocalDateTime())).map(ProjectInfoResponse::new).collect(toList()))
                .build();
    }

    public static AddUserResponse projectToAddUserResponse(Project project) {
        return AddUserResponse.builder()
                .projectId(project.getId())
                .name(project.getProjectName())
                .description(project.getDescription())
                .localDate(project.getLocalDateTime().toLocalDate())
                .ownerName(project.getOwnerName())
                .memberNames(project.getUsers().stream().map(User::getUsername).collect(toList()))
                .projectInfoResponseList(project.getInfoList().stream().sorted((i1, i2) -> -i1.getLocalDateTime().compareTo(i2.getLocalDateTime())).map(ProjectInfoResponse::new).collect(toList()))
                .build();
    }

    public static AddMyInfoToOwnedProjectResponse projectToAddInfoToOwnedProjectResponse(Project project) {
        return AddMyInfoToOwnedProjectResponse.builder()
                .projectId(project.getId())
                .name(project.getProjectName())
                .description(project.getDescription())
                .localDate(project.getLocalDateTime().toLocalDate())
                .ownerName(project.getOwnerName())
                .memberNames(project.getUsers().stream().map(User::getUsername).collect(toList()))
                .projectInfoResponseList(project.getInfoList().stream().sorted((i1, i2) -> -i1.getLocalDateTime().compareTo(i2.getLocalDateTime())).map(ProjectInfoResponse::new).collect(toList()))
                .build();
    }

    public static RemoveInfoFromOwnedProjectResponse projectToRemoveInfoFromOwnedProjectResponse(Project project) {
        return RemoveInfoFromOwnedProjectResponse.builder()
                .projectId(project.getId())
                .name(project.getProjectName())
                .description(project.getDescription())
                .localDate(project.getLocalDateTime().toLocalDate())
                .ownerName(project.getOwnerName())
                .memberNames(project.getUsers().stream().map(User::getUsername).collect(toList()))
                .projectInfoResponseList(project.getInfoList().stream().sorted((i1, i2) -> -i1.getLocalDateTime().compareTo(i2.getLocalDateTime())).map(ProjectInfoResponse::new).collect(toList()))
                .build();
    }

    public static RemoveUserFromOwnedProjectResponse projectToRemoveUserFromOwnedProjectResponse(Project project) {
        return RemoveUserFromOwnedProjectResponse.builder()
                .projectId(project.getId())
                .name(project.getProjectName())
                .description(project.getDescription())
                .localDate(project.getLocalDateTime().toLocalDate())
                .ownerName(project.getOwnerName())
                .memberNames(project.getUsers().stream().map(User::getUsername).collect(toList()))
                .projectInfoResponseList(project.getInfoList().stream().sorted((i1, i2) -> -i1.getLocalDateTime().compareTo(i2.getLocalDateTime())).map(ProjectInfoResponse::new).collect(toList()))
                .build();
    }

    public static AddMyInfoToOtherProjectResponse projectToAddMyInfoToOtherProjectResponse(Project project) {
        return AddMyInfoToOtherProjectResponse.builder()
                .projectId(project.getId())
                .name(project.getProjectName())
                .description(project.getDescription())
                .localDate(project.getLocalDateTime().toLocalDate())
                .ownerName(project.getOwnerName())
                .memberNames(project.getUsers().stream().map(User::getUsername).collect(toList()))
                .projectInfoResponseList(project.getInfoList().stream().sorted((i1, i2) -> -i1.getLocalDateTime().compareTo(i2.getLocalDateTime())).map(ProjectInfoResponse::new).collect(toList()))
                .build();
    }

    public static RemoveMyInfoFromOtherProjectResponse projectToRemoveMyInfoFromOtherProjectResponse(Project project) {
        return RemoveMyInfoFromOtherProjectResponse.builder()
                .projectId(project.getId())
                .name(project.getProjectName())
                .description(project.getDescription())
                .localDate(project.getLocalDateTime().toLocalDate())
                .ownerName(project.getOwnerName())
                .memberNames(project.getUsers().stream().map(User::getUsername).collect(toList()))
                .projectInfoResponseList(project.getInfoList().stream().sorted((i1, i2) -> -i1.getLocalDateTime().compareTo(i2.getLocalDateTime())).map(ProjectInfoResponse::new).collect(toList()))
                .build();
    }

    public static RemoveMyFromOtherProjectResponse projectToRemoveMyFromOtherProjectResponse(Project project) {
        return RemoveMyFromOtherProjectResponse.builder()
                .projectId(project.getId())
                .name(project.getProjectName())
                .description(project.getDescription())
                .localDate(project.getLocalDateTime().toLocalDate())
                .ownerName(project.getOwnerName())
                .memberNames(project.getUsers().stream().map(User::getUsername).collect(toList()))
                .projectInfoResponseList(project.getInfoList().stream().sorted((i1, i2) -> -i1.getLocalDateTime().compareTo(i2.getLocalDateTime())).map(ProjectInfoResponse::new).collect(toList()))
                .build();
    }
}
