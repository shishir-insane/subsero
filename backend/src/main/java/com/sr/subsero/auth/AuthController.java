package com.sr.subsero.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
        String jwt = jwtUtils.generateToken(authentication.getName());
        return jwt;
    }

    @GetMapping("/validate")
    public String validateToken(@RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        if (jwtUtils.validateToken(token)) {
            return "Token is valid!";
        }
        return "Invalid token!";
    }
}