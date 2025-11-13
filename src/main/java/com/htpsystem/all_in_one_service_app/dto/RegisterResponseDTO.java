package com.htpsystem.all_in_one_service_app.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RegisterResponseDTO {
    private String message;
    private Long userId;
    private String email;
    private LocalDateTime timestamp;
}
