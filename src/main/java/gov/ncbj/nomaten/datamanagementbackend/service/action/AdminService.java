package gov.ncbj.nomaten.datamanagementbackend.service.action;

import gov.ncbj.nomaten.datamanagementbackend.dto.adminauth.admin.*;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.repository.UserRepository;
import gov.ncbj.nomaten.datamanagementbackend.service.support.AuthService;
import gov.ncbj.nomaten.datamanagementbackend.service.support.CheckService;
import gov.ncbj.nomaten.datamanagementbackend.service.support.FolderService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.FileSystems.getDefault;
import static java.time.Instant.now;
import static gov.ncbj.nomaten.datamanagementbackend.constants.Constants.STORAGE;

@Service
@AllArgsConstructor
@Transactional
public class AdminService {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final FolderService folderService;
    private final CheckService checkService;
    private final PasswordEncoder passwordEncoder;

    public List<String> getStorageUsers() {
        Path path = getDefault().getPath(STORAGE);
        return folderService.getDirectSubfolders(path);
    }

    public List<String> createStorageUser(CreateStorageUserRequest createStorageUserRequest) throws IOException {
        Path path = getDefault().getPath(STORAGE);
        Path userPath = getDefault().getPath(STORAGE, createStorageUserRequest.getUserName());
        checkService.folderDoesNotExist(userPath, "User " + createStorageUserRequest.getUserName() + " already exists");
        folderService.createFolder(userPath);
        return folderService.getDirectSubfolders(path);
    }

    public List<String> deleteStorageUser(DeleteUserRequest deleteUserRequest) throws IOException {
        Path path = getDefault().getPath(STORAGE);
        Path userPath = getDefault().getPath(STORAGE, deleteUserRequest.getUserName());
        checkService.folderExists(userPath, "Main folder of the user "
                + deleteUserRequest.getUserName() + " does not exist");
        checkService.folderIsEmpty(userPath, "Cannot remove user with data");
        folderService.deleteItem(userPath);
        return folderService.getDirectSubfolders(path);
    }

    public List<User> getDbUsers() {
        return authService.getUsers();
    }

    @Transactional
    public void createDbUser(CreateDbUserRequest createDbUserRequest) {
        checkService.userDoesNotExist(createDbUserRequest.getUserName());
        User user = User.builder()
            .username(createDbUserRequest.getUserName())
            .email(createDbUserRequest.getEmail())
            .password(passwordEncoder.encode(createDbUserRequest.getPassword()))
            .created(now())
            .roles(createDbUserRequest.getRole())
            .enabled(true)
            .build();
        userRepository.save(user);
    }

    @Transactional
    public void deleteDbUser(DeleteUserRequest deleteUserRequest) {
        User user = authService.getUserByName(deleteUserRequest.getUserName());
        if(user.isEnabled()) {
            throw new RuntimeException("Active user can not be deleted");
        }
        if(user.getRoles().equals("ROLE_ADMIN")) {
            throw new RuntimeException("Admin user can not be deleted");
        }
        if(user.getInfoList().size() != 0) {
            throw new RuntimeException("User with data can not be deleted");
        }
        if(user.getProjects().size() != 0) {
            throw new RuntimeException("User with projects can not be deleted");
        }
        userRepository.delete(user);
    }

    @Transactional
    public void activateUser(ActivateUserRequest activateUserRequest) {
        User user = authService.getUserByName(activateUserRequest.getUserName());
        if(user.isEnabled()) {
            throw new RuntimeException("The user " + activateUserRequest.getUserName() + " is active");
        }
        user.setEnabled(true);
    }

    @Transactional
    public void deactivateUser(DeactivateUserRequest deactivateUserRequest) {
        User user = authService.getUserByName(deactivateUserRequest.getUserName());
        if(!user.isEnabled()) {
            throw new RuntimeException("The user " + deactivateUserRequest.getUserName() + " is inactive");
        }
        if(user.getRoles().equals("ROLE_ADMIN")) {
            throw new RuntimeException("Cannot deactivate ADMIN user");
        }
        user.setEnabled(false);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        User user = authService.getUserByName(resetPasswordRequest.getUserName());
        if(user.getRoles().equals("ROLE_ADMIN")) {
            throw new RuntimeException("Cannot reset ADMIN user password");
        }
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
    }

}
