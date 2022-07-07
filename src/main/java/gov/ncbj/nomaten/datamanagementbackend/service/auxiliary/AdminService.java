package gov.ncbj.nomaten.datamanagementbackend.service.auxiliary;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_admin.*;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.repository.StorageRepository;
import gov.ncbj.nomaten.datamanagementbackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.FileSystems.getDefault;
import static java.time.Instant.now;
import static gov.ncbj.nomaten.datamanagementbackend.constants.Constants.STORAGE;

@Service
@AllArgsConstructor
@Transactional
public class AdminService {

    private final UserRepository userRepository;
    private final StorageRepository storageRepository;
    private final PasswordEncoder passwordEncoder;

    public List<String> getStorageUsers() {
        Path path = getDefault().getPath(STORAGE);
        return storageRepository.getDirectSubfolders(path);
    }

    public List<String> createStorageUser(CreateStorageUserRequest createStorageUserRequest) throws IOException {
        Path path = getDefault().getPath(STORAGE);
        return storageRepository.createSubfolder(path, createStorageUserRequest.getUserName());
    }

    public List<String> deleteStorageUser(DeleteUserRequest deleteUserRequest) throws IOException {
        Path path = getDefault().getPath(STORAGE);
        Path userPath = getDefault().getPath(STORAGE, deleteUserRequest.getUserName());
        if(!storageRepository.folderExists(userPath)) {
            throw new RuntimeException("Main folder of the user " + deleteUserRequest.getUserName() + " does not exits");
        }
        List<String> userData = storageRepository.getDirectSubfolders(userPath);
        if(userData.size() > 0) {
            throw new RuntimeException("Cannot remove user with data");
        } else {
            return storageRepository.deleteSubfolder(path, deleteUserRequest.getUserName());
        }
    }

    public List<String> getDbUsers() {
        return userRepository.findAll().stream().map(User::getUsername).sorted().collect(Collectors.toList());
    }

    @Transactional
    public void createDbUser(CreateDbUserRequest createDbUserRequest) {
        if(userRepository.findByUsername(createDbUserRequest.getUserName()).isPresent()) {
            throw new RuntimeException("User " + createDbUserRequest.getUserName() + " already exist");
        }

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
        User user = userRepository.findByUsername(deleteUserRequest.getUserName())
                .orElseThrow(() -> new RuntimeException("No user " + deleteUserRequest.getUserName()));
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
        User user = userRepository.findByUsername(activateUserRequest.getUserName())
                .orElseThrow(() -> new RuntimeException("No user " + activateUserRequest.getUserName()));
        if(user.isEnabled()) {
            throw new RuntimeException("The user " + activateUserRequest.getUserName() + " is active");
        }
        user.setEnabled(true);
    }

    @Transactional
    public void deactivateUser(DeactivateUserRequest deactivateUserRequest) {
        User user = userRepository.findByUsername(deactivateUserRequest.getUserName())
                .orElseThrow(() -> new RuntimeException("No user " + deactivateUserRequest.getUserName()));
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
        User user = userRepository.findByUsername(resetPasswordRequest.getUserName())
                .orElseThrow(() -> new RuntimeException("No user " + resetPasswordRequest.getUserName()));
        if(user.getRoles().equals("ROLE_ADMIN")) {
            throw new RuntimeException("Cannot reset ADMIN user password");
        }
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
    }

}
