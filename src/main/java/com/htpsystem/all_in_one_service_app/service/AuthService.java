package com.htpsystem.all_in_one_service_app.service;

import com.htpsystem.all_in_one_service_app.dto.RegisterRequestDTO;
import com.htpsystem.all_in_one_service_app.dto.RegisterResponseDTO;
import com.htpsystem.all_in_one_service_app.entity.Gender;
import com.htpsystem.all_in_one_service_app.entity.Role;
import com.htpsystem.all_in_one_service_app.entity.User;
import com.htpsystem.all_in_one_service_app.entity.UserData;
import com.htpsystem.all_in_one_service_app.repository.GenderRepository;
import com.htpsystem.all_in_one_service_app.repository.RoleReposistory;
import com.htpsystem.all_in_one_service_app.repository.UserDataRepository;
import com.htpsystem.all_in_one_service_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class AuthService {

    private final UserRepository userRepository;
    private final RoleReposistory roleReposistory;
    private final GenderRepository genderRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDataRepository userDataRepository;

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

}
