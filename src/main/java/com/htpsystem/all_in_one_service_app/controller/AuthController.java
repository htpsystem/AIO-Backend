package com.htpsystem.all_in_one_service_app.controller;

import com.htpsystem.all_in_one_service_app.dto.LoginRequestDTO;
import com.htpsystem.all_in_one_service_app.dto.LoginResponseDTO;
import com.htpsystem.all_in_one_service_app.dto.RegisterRequestDTO;
import com.htpsystem.all_in_one_service_app.dto.RegisterResponseDTO;
import com.htpsystem.all_in_one_service_app.entity.EmailVerificationToken;
import com.htpsystem.all_in_one_service_app.entity.User;
import com.htpsystem.all_in_one_service_app.repository.EmailVerificationTokenRepository;
import com.htpsystem.all_in_one_service_app.repository.UserRepository;
import com.htpsystem.all_in_one_service_app.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> registerUser(@RequestBody RegisterRequestDTO requestDTO){
        RegisterResponseDTO responseDTO = authService.register(requestDTO);

        if ("EXISTS".equalsIgnoreCase(responseDTO.getMessage())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseDTO);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody LoginRequestDTO requestDTO) {

        try {
            LoginResponseDTO response = authService.login(requestDTO);
            return ResponseEntity.ok(response);  // 200 OK
        }

        catch (RuntimeException e) {
            e.printStackTrace();
            if (e.getMessage().equalsIgnoreCase("User NotFound")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(LoginResponseDTO.builder()
                                .message("User Not Found")
                                .build());
            }

            if (e.getMessage().equalsIgnoreCase("Invalid credentials")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(LoginResponseDTO.builder()
                                .message("Invalid credentials")
                                .build());
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(LoginResponseDTO.builder()
                            .message("Something went wrong")
                            .build());
        }
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam String token){
        EmailVerificationToken emailToken = emailVerificationTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid Token"));
        if(emailToken.getExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token Expired");
        }


        User user = emailToken.getUser();
        user.setVerified(true);
        userRepository.save(user);

        return "Email Verified Successfully! You can now login.";
    }
}
