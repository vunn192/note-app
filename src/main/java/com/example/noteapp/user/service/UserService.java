package com.example.noteapp.user.service;

import com.example.noteapp.auth.model.AuthenticationResponse;
import com.example.noteapp.common.exception.UserNotFound;
import com.example.noteapp.common.jwt.JwtService;
import com.example.noteapp.user.dto.UserCreationDto;
import com.example.noteapp.user.model.User;
import com.example.noteapp.user.reposity.UserReposity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserReposity userReposity;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    public User createUser(UserCreationDto userCreationDto) {
        User user = modelMapper.map(userCreationDto, User.class);
        user.setPassword(passwordEncoder.encode(userCreationDto.getPassword()));
        user.setCreatedAt(new Date(System.currentTimeMillis()));
        user.setUpdatedAt(new Date(System.currentTimeMillis()));
        userReposity.save(user);
        return user;
    }
    public User findByUsername(String username) {
        return userReposity.findByUsername(username)
                .orElseThrow(() -> new UserNotFound(String.format("User with username %s not found", username)));
    }
    public void updateUserPassword(long id, String password) {
        User user = userReposity.findById(id)
                .orElseThrow(() -> new UserNotFound(String.format("User with id %d not found", id)));

        user.setPassword(passwordEncoder.encode(password));
        user.setUpdatedAt(new Date(System.currentTimeMillis()));
        userReposity.save(user);
    }
    public AuthenticationResponse forgetPassword(String username) {
        User user = findByUsername(username);

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", user.getId());

        String token = jwtService.generateToken(extraClaims, user);
        return new AuthenticationResponse(token);
    }
}
