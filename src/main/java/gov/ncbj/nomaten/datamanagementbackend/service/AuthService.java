package gov.ncbj.nomaten.datamanagementbackend.service;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_auth.AuthenticationResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_auth.LoginRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_auth.RefreshTokenRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.my_auth.RegisterRequest;
import gov.ncbj.nomaten.datamanagementbackend.exception.CustomException;
import gov.ncbj.nomaten.datamanagementbackend.model.NotificationEmail;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.model.VerificationToken;
import gov.ncbj.nomaten.datamanagementbackend.repository.UserRepository;
import gov.ncbj.nomaten.datamanagementbackend.repository.VerificationTokenRepository;
import gov.ncbj.nomaten.datamanagementbackend.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.time.Instant.now;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(now());
        user.setEnabled(false);
        userRepository.save(user);

        String token = generateVerificationToken(user);

        mailService.sendMail(new NotificationEmail("Please Activate your Account", user.getEmail(),
                "Thank you for signing up to NomatenData. Please click on the below url to activate your account http://localhost:8080/api/auth/accountVerification/" +
                token));

        createFolderInStorage(user);
    }

    private void createFolderInStorage(User user) {
        Path path = FileSystems.getDefault().getPath("storage", user.getUsername());
        try {
            Files.createDirectory(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);
        verificationTokenOptional.orElseThrow(() -> new CustomException("Invalid Token"));
        fetchUserAndEnable(verificationTokenOptional.get());
    }

    @Transactional // this is done differently
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        verificationToken.getUser().setEnabled(true);
        verificationTokenRepository.save(verificationToken);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();
    }

    @Transactional // this is done differently
    public User getCurrentUser() {
        String loggedUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(loggedUserName)
                .orElseThrow(() -> new CustomException("No user " + loggedUserName + " found"));
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

    @Transactional
    public User getUserByName(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("No user " + username + " found"));
    }

    public List<String> getUsers() {
        return userRepository.findAll().stream().map(User::getUsername).collect(Collectors.toList());
    }

}