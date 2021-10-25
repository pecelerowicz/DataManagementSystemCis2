package gov.ncbj.nomaten.datamanagementbackend.service;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_admin.CreateDbUserRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_admin.CreateStorageUserRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_admin.DeleteUserRequest;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.model.VerificationToken;
import gov.ncbj.nomaten.datamanagementbackend.repository.StorageRepository;
import gov.ncbj.nomaten.datamanagementbackend.repository.UserRepository;
import gov.ncbj.nomaten.datamanagementbackend.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static gov.ncbj.nomaten.datamanagementbackend.util.DataManipulation.STORAGE;
import static java.nio.file.FileSystems.getDefault;
import static java.time.Instant.now;

@Service
@AllArgsConstructor
@Transactional
public class AdminService {

    private final UserRepository userRepository;
    private final StorageRepository storageRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;


    public List<String> getDbUsers() {
        return userRepository.findAll().stream().map(User::getUsername).collect(Collectors.toList());
    }

    @Transactional
    public void createDbUser(CreateDbUserRequest createDbUserRequest) {
        if(userRepository.findByUsername(createDbUserRequest.getUsername()).isPresent()) {
            throw new RuntimeException("User " + createDbUserRequest.getUsername() + " already exist");
        }

        User user = User.builder()
            .username(createDbUserRequest.getUsername())
            .email(createDbUserRequest.getEmail())
            .password(createDbUserRequest.getPassword())
            .created(now())
            .roles("ROLE_USER")
            .enabled(true)
            .build();

        userRepository.save(user);
    }

    public List<String> deleteDbUser() {
        // nie usuwać admina
        return null;
    }

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

}
