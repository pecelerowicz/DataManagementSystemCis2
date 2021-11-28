package gov.ncbj.nomaten.datamanagementbackend.controller;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_auth.*;
import gov.ncbj.nomaten.datamanagementbackend.service.AuthService;
import gov.ncbj.nomaten.datamanagementbackend.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(OK).body("Refresh Token deleted successfully.");
    }

    @PutMapping("/newpass")
    public ResponseEntity<ChangePasswordResponse> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        authService.changePassword(changePasswordRequest);
        return ResponseEntity.status(OK).body(ChangePasswordResponse.builder().message("Password changed").build());
    }

    // TODO zrobić porządne dto
    @GetMapping("/users")
    public ResponseEntity<List<String>> getUsers() {
        return ResponseEntity.status(OK).body(authService.getUsers());
    }

}
