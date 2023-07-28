package com.example.noteapp.auth.service;

import com.example.noteapp.auth.model.AuthenticationRequest;
import com.example.noteapp.auth.model.AuthenticationResponse;
import com.example.noteapp.common.jwt.JwtService;
import com.example.noteapp.user.model.User;
import com.example.noteapp.user.reposity.UserReposity;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserReposity userReposity;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse authentication(AuthenticationRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );

        authenticationManager.authenticate(authToken);
        User user = userReposity.findByUsername(request.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException(1));

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", user.getId());

        String token = jwtService.generateToken(extraClaims, user);
        return new AuthenticationResponse(token);
    }
}
