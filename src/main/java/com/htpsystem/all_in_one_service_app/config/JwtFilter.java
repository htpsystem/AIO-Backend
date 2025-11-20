package com.htpsystem.all_in_one_service_app.config;

import com.htpsystem.all_in_one_service_app.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
//OncePerRequestFilter → Spring ensures this filter runs only once per request.
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
//Constructor injection
//jwtUtil is your helper class used for extracting email and validating token.
//Spring injects it automatically because of @Component.
    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //Read Authorization Header
        String header = request.getHeader("Authorization");
        //Check if Header Contains a Bearer Token
        //Only process if properly formatted.
        if (header != null && header.startsWith("Bearer ")) {
            //Extract Token from Header
            //"Bearer " is 7 characters.
            //Removes "Bearer " and extracts only:
            String token = header.substring(7);

            try {
                //Validate Token
                //Your JwtUtil validates:
                //
                //Signature
                //
                //Expiry
                //
                //Structure
                //
                //And returns the email inside token.
                String email = jwtUtil.validateToken(token);
                //Create a Spring Security User Object
                //Creates an authenticated user object using the email.
                //Empty password and empty roles list for now.
                User user = new User(email, "", Collections.emptyList());

                // Create Authentication Token
                //This object tells Spring:
                //Who the user is
                //
                //What roles the user has
                //
                //That user is authenticated
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                // Attach Request Details
                //Adds details like:
                //IP address
                // Session information
                // (Useful for logs and debugging)
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Set Authentication in Spring Security Context
                // This is the MOST IMPORTANT LINE.
                // It tells Spring Security:
                //“This request is authenticated. This is the logged-in user.”
//                After this point →
//                Your controllers can use
//                @AuthenticationPrincipal
//                Your routes detect authentication
//                Authorization based on roles works
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                System.out.println("Invalid JWT: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}

