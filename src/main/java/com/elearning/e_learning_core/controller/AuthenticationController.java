package com.elearning.e_learning_core.controller;

import com.elearning.e_learning_core.Dtos.ApiResponse;
import com.elearning.e_learning_core.Dtos.AuthenticationDTO;
import com.elearning.e_learning_core.Dtos.UserRegisterDTO;
import com.elearning.e_learning_core.model.OnRegistrationCompleteEvent;
import com.elearning.e_learning_core.model.Usr;
import com.elearning.e_learning_core.model.VerificationToken;
import com.elearning.e_learning_core.service.AuthenticationService;
import com.elearning.e_learning_core.service.UsrService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController {
    @Autowired
    ApplicationEventPublisher eventPublisher;
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Autowired
    private UsrService service;

    @GetMapping("/registrationConfirm")
    public ResponseEntity<ApiResponse<?>> confirmRegistration(@RequestParam("token") String token) {

        VerificationToken verificationToken = service.getVerificationToken(token);
        if (verificationToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("error", "Token inválido ou expirado", 400, null));
        }

        Usr user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("error", "Token expirado", 400, null));
        }

        user.setEnabled(true);
        service.saveRegisteredUser(user);

        return ResponseEntity.ok(new ApiResponse<>("success", "Conta ativada com sucesso!", 200, null));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody UserRegisterDTO userRegisterDTO,
            HttpServletRequest request) {

        ApiResponse<?> response = authenticationService.registerUser(userRegisterDTO);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(response.getCode()).body(response);
        } else {
            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(((Usr) response.getData()),
                    request.getLocale(), appUrl));
        }

        return ResponseEntity.status(response.getCode())
                .body(new ApiResponse<>(response.getStatus(), response.getMessage(), response.getCode(), "null"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody AuthenticationDTO dto) {

        ApiResponse<?> response = authenticationService.authenticateUser(dto);

        return ResponseEntity.ok(response);
    }

}
