package com.htpsystem.all_in_one_service_app.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {
    private String message;
    private String token;
    private String email;
}
