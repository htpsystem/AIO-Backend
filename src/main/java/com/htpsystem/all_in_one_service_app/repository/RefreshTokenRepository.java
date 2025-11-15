package com.htpsystem.all_in_one_service_app.repository;

import com.htpsystem.all_in_one_service_app.entity.RefreshToken;
import com.htpsystem.all_in_one_service_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);

    void deleteByUser(User user);

}
