package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_admin.CreateDbUserRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_admin.CreateStorageUserRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_admin.DeleteUserRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_admin.UsersResponse;
import gov.ncbj.nomaten.datamanagementbackend.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
@CrossOrigin
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/db/users")
    public ResponseEntity<UsersResponse> getDbUsers() {
        return ResponseEntity.status(OK)
                .body(UsersResponse.builder().users(adminService.getDbUsers()).build());
    }

    @PostMapping("/db/users")
    public ResponseEntity<UsersResponse> createDbUser(@RequestBody CreateDbUserRequest createDbUserRequest) {
        // TODO validation
        adminService.createDbUser(createDbUserRequest);
        return ResponseEntity.status(OK)
                .body(UsersResponse.builder().users(adminService.getDbUsers()).build());
    }

    @DeleteMapping("/db/users")
    public ResponseEntity<UsersResponse> deleteDbUser() {
        // TODO validation
        return ResponseEntity.status(OK)
                .body(UsersResponse.builder().users(adminService.deleteDbUser()).build());

    }

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
        return ResponseEntity.status(OK)
                .body(UsersResponse.builder().users(adminService.deleteStorageUser(deleteUserRequest)).build());
    }

}
