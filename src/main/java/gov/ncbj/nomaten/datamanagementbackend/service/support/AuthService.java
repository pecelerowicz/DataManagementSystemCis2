package gov.ncbj.nomaten.datamanagementbackend.service.support;

import gov.ncbj.nomaten.datamanagementbackend.dto.adminauth.auth.ChangePasswordRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.adminauth.auth.LoginRequest;
import gov.ncbj.nomaten.datamanagementbackend.dto.adminauth.auth.LoginResponse;
import gov.ncbj.nomaten.datamanagementbackend.dto.adminauth.auth.RefreshTokenRequest;
import gov.ncbj.nomaten.datamanagementbackend.exception.CustomException;
import gov.ncbj.nomaten.datamanagementbackend.model.User;
import gov.ncbj.nomaten.datamanagementbackend.repository.UserRepository;
import gov.ncbj.nomaten.datamanagementbackend.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static java.time.Instant.now;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        return LoginResponse.builder()
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

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }

    public LoginResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        return LoginResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

    @Transactional
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = getCurrentUser();
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
    }

    @Transactional
    public User getUserByName(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("No user " + username + " found"));
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

}