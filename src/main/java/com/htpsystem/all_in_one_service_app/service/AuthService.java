package com.htpsystem.all_in_one_service_app.service;

import com.htpsystem.all_in_one_service_app.dto.LoginRequestDTO;
import com.htpsystem.all_in_one_service_app.dto.LoginResponseDTO;
import com.htpsystem.all_in_one_service_app.dto.RegisterRequestDTO;
import com.htpsystem.all_in_one_service_app.dto.RegisterResponseDTO;
import com.htpsystem.all_in_one_service_app.entity.*;
import com.htpsystem.all_in_one_service_app.repository.*;
import com.htpsystem.all_in_one_service_app.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class AuthService {

    private final UserRepository userRepository;
    private final RoleReposistory roleReposistory;
    private final GenderRepository genderRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDataRepository userDataRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final JwtUtil jwtUtil;

    public RegisterResponseDTO  register(RegisterRequestDTO requestDTO) {
        if (userRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            return RegisterResponseDTO.builder()
                    .message("EXISTS")
                    .build();
        }
        // If not, create new user and save
        // Encrypt password before saving
        String encodedPassword = passwordEncoder.encode(requestDTO.getPassword());

        //Fetch role from DB (or throw error if not found)
        Role role = roleReposistory.findByName(requestDTO.getRole())
                .orElseThrow(() -> new RuntimeException("Role Not Found"));

        //Fetch genderId from gender type
        Gender gender = genderRepository.findByType(requestDTO.getGender())
                .orElseThrow(() -> new RuntimeException("Gender Not Found"));

        User user = User.builder()
                .email(requestDTO.getEmail())
                .password(encodedPassword)
                .role(role)
                .isActive(true)
                .build();
        User savedUser = userRepository.save(user);

        UserData userData = UserData.builder()
                .first_name(requestDTO.getFirstName())
                .last_name(requestDTO.getLastName())
                .gender(gender)
                .dob(requestDTO.getDob())
                .user(user)
                .build();

     userDataRepository.save(userData);

        return RegisterResponseDTO.builder()
                .message("User registered successfully!")
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public LoginResponseDTO login(LoginRequestDTO requestDTO) {
      User user = userRepository.findByEmail(requestDTO.getEmail())
              .orElseThrow(()->new RuntimeException("User NotFound"));
      if(!passwordEncoder.matches(requestDTO.getPassword(), user.getPassword())) {
          throw new RuntimeException("Invalid credentials");
      }

      if(!user.isVerified()) {
          // 1. Create verification token
          String token = UUID.randomUUID().toString();

          EmailVerificationToken emailToken =  EmailVerificationToken.builder()
                  .token(token)
                  .expiry(LocalDateTime.now().plusMinutes(30))
                  .user(user)
                  .build();

          emailVerificationTokenRepository.save(emailToken);

          // 2. Send verification email (link)
          String link = "http://localhost:8081/api/auth/verify-email?token=" + token;
          System.out.println("Verification Link: " + link);

          return LoginResponseDTO.builder()
                 .message("Please verify your email. Verification link sent."+link)
                  .token(null)
                  .build();
      }

      // ✔ VERIFIED USER → Generate JWT
      String token = jwtUtil.generateToken(user.getEmail());

        return LoginResponseDTO.builder()
                .token(token)
                .message("Login Successfully!")
                .email(user.getEmail())
                .build();
    }
}
