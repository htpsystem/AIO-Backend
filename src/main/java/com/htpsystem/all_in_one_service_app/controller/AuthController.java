package com.htpsystem.all_in_one_service_app.controller;

import com.htpsystem.all_in_one_service_app.dto.RegisterRequestDTO;
import com.htpsystem.all_in_one_service_app.dto.RegisterResponseDTO;
import com.htpsystem.all_in_one_service_app.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> registerUser(@RequestBody RegisterRequestDTO requestDTO){
        RegisterResponseDTO responseDTO = authService.register(requestDTO);

        if ("EXISTS".equalsIgnoreCase(responseDTO.getMessage())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseDTO);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}
