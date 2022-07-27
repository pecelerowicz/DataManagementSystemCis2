package gov.ncbj.nomaten.datamanagementbackend.controller.adminauth;

import gov.ncbj.nomaten.datamanagementbackend.dto.my_auth.*;
import gov.ncbj.nomaten.datamanagementbackend.service.support.AuthService;
import gov.ncbj.nomaten.datamanagementbackend.service.support.RefreshTokenService;
import gov.ncbj.nomaten.datamanagementbackend.validators.my_auth.ChangePasswordRequestValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/auth/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/auth/refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(OK).body("Refresh Token deleted successfully.");
    }

    @PutMapping("/newpass")
    public ResponseEntity<ChangePasswordResponse> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        ChangePasswordRequestValidator.builder().build().validate(changePasswordRequest);
        authService.changePassword(changePasswordRequest);
        return ResponseEntity.status(OK).body(ChangePasswordResponse.builder().message("Password changed").build());
    }

//    // TODO zrobić porządne dto
//    @GetMapping("/auth/users")
//    public ResponseEntity<List<String>> getUsers() {
//        return ResponseEntity.status(OK).body(authService.getUsers());
//    }

}
