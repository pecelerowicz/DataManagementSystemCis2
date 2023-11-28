package gov.ncbj.nomaten.datamanagementbackend.controller.adminauth;

import gov.ncbj.nomaten.datamanagementbackend.dto.adminauth.auth.*;
import gov.ncbj.nomaten.datamanagementbackend.service.support.AuthService;
import gov.ncbj.nomaten.datamanagementbackend.service.support.RefreshTokenService;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_auth.ChangePasswordRequestValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/auth/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
        authService.signup(registerRequest);
        return new ResponseEntity<>("User registration successful.", OK);
    }

    @PostMapping("/auth/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        // TODO validation
        return authService.login(loginRequest);
    }

    @PostMapping("/auth/refresh/token") // TODO probably remove /token
    public LoginResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        // TODO validation
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        // TODO validation
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ok("Refresh Token deleted successfully.");
    }

    @PutMapping("/newpass") // TODO add /auth
    public ResponseEntity<ChangePasswordResponse> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        ChangePasswordRequestValidator.builder().build().validate(changePasswordRequest);
        authService.changePassword(changePasswordRequest);
        return ok(ChangePasswordResponse.builder().message("Password changed").build());
    }

}
