package gov.ncbj.nomaten.datamanagementbackend.controller.adminauth;

import gov.ncbj.nomaten.datamanagementbackend.comparator.UserComparator;
import gov.ncbj.nomaten.datamanagementbackend.dto.admin.*;
import gov.ncbj.nomaten.datamanagementbackend.dto.adminauth.admin.*;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.service.action.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
@CrossOrigin
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/storage/users")
    public ResponseEntity<UsersResponse> getStorageUsers() {
        return ResponseEntity.status(OK)
                .body(UsersResponse.builder().users(adminService.getStorageUsers()).build());
    }

    @PostMapping("/storage/users")
    public ResponseEntity<UsersResponse> createStorageUser(@RequestBody CreateStorageUserRequest createStorageUserRequest) throws IOException {
        // TODO validation
        return ResponseEntity.status(OK)
                .body(UsersResponse.builder().users(adminService.createStorageUser(createStorageUserRequest)).build());
    }

    @DeleteMapping("/storage/users")
    public ResponseEntity<UsersResponse> deleteStorageUser(@RequestBody DeleteUserRequest deleteUserRequest) throws IOException {
        // TODO validation
        return ResponseEntity.status(OK)
                .body(UsersResponse.builder().users(adminService.deleteStorageUser(deleteUserRequest)).build());
    }

    @GetMapping("/db/users")
    public ResponseEntity<UsersResponse> getDbUsers() {
        return ResponseEntity.status(OK)
                .body(UsersResponse.builder().users(adminService.getDbUsers()
                        .stream()
                        .sorted(new UserComparator())   // TODO should be changed
                        .map(User::getUsername)
                        .collect(Collectors.toList())).build());
    }

    @PostMapping("/db/users")
    public ResponseEntity<UsersResponse> createDbUser(@RequestBody CreateDbUserRequest createDbUserRequest) {
        // TODO validation
        adminService.createDbUser(createDbUserRequest);
        return ResponseEntity.status(OK)
                .body(UsersResponse.builder().users(adminService.getDbUsers()
                        .stream()
                        .sorted(new UserComparator())   // TODO should be changed
                        .map(User::getUsername)
                        .collect(Collectors.toList())).build());
    }

    @DeleteMapping("/db/users")
    public ResponseEntity<UsersResponse> deleteDbUser(@RequestBody DeleteUserRequest deleteUserRequest) {
        // TODO validation
        adminService.deleteDbUser(deleteUserRequest);
        return ResponseEntity.status(OK)
                .body(UsersResponse.builder().users(adminService.getDbUsers()
                        .stream()
                        .sorted(new UserComparator())   // TODO should be changed
                        .map(User::getUsername)
                        .collect(Collectors.toList())).build());
    }

    @PutMapping("/db/users/activate")
    public ResponseEntity<ActivateUserResponse> activateUser(@RequestBody ActivateUserRequest activateUserRequest) {
        // TODO validation
        adminService.activateUser(activateUserRequest);
        return ResponseEntity.status(OK)
                .body(ActivateUserResponse.builder().message("User " + activateUserRequest.getUserName() + " was enabled").build());
    }

    @PutMapping("/db/users/deactivate")
    public ResponseEntity<DeactivateUserResponse> deactivateUser(@RequestBody DeactivateUserRequest deactivateUserRequest) {
        // TODO validation
        adminService.deactivateUser(deactivateUserRequest);
        return ResponseEntity.status(OK)
                .body(DeactivateUserResponse.builder().message("User " + deactivateUserRequest.getUserName() + " was disabled").build());
    }

    @PutMapping("/db/users/reset")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        // TODO validation
        adminService.resetPassword(resetPasswordRequest);
        return ResponseEntity.status(OK)
                .body(ResetPasswordResponse.builder().message("Password of the user " + resetPasswordRequest.getUserName() + " was reset").build());
    }

}
