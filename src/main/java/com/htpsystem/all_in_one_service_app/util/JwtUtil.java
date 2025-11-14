package com.htpsystem.all_in_one_service_app.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET = "7dF!93KfL@aP09s#Qw1lZ$X7eU8*gH3kBvN5@Fs!xT2RzP0nM3qW#yD8cL&V9b";
    private final long EXPIRATION = 1000L * 60 * 60 * 24 * 30; // 24 hours

    public String generateToken(String email){
        return JWT.create()
                .withSubject(email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public String validateToken(String token){
        return JWT.require(Algorithm.HMAC256(SECRET))
                .build()
                .verify(token)
                .getSignature();
    }
}
