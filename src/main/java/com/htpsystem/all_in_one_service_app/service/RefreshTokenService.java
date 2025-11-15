package com.htpsystem.all_in_one_service_app.service;

import com.htpsystem.all_in_one_service_app.entity.RefreshToken;
import com.htpsystem.all_in_one_service_app.entity.User;
import com.htpsystem.all_in_one_service_app.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public String createRefreshToken(User user) {
        // Delete old token (one token per user)
        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiry(LocalDateTime.now().plusDays(30)); // 30 days validity

        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    public boolean  isExpiredToken(RefreshToken token) {
        return token.getExpiry().isBefore(LocalDateTime.now());
    }
}
