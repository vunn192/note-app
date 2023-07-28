package com.example.noteapp.user.controller;

import com.example.noteapp.auth.model.AuthenticationResponse;
import com.example.noteapp.common.util.CookieUtil;
import com.example.noteapp.common.ResponseHandler;
import com.example.noteapp.common.jwt.JwtService;
import com.example.noteapp.user.dto.UserCreationDto;
import com.example.noteapp.user.model.User;
import com.example.noteapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    @PostMapping(value = "/register")
    public ResponseEntity<Object> register(@RequestBody UserCreationDto userCreationDto) {
        User createdUser = userService.createUser(userCreationDto);
        UserCreationDto creationDto = modelMapper.map(createdUser, UserCreationDto.class);
        return ResponseHandler.generateResponse("Successful create account", HttpStatus.CREATED, creationDto);
    }

    @PostMapping(value = "forget-password")
    public ResponseEntity<Object> forgetPassword(@RequestBody String body, HttpServletResponse response) {
        JSONObject jo = new JSONObject(body);
        String username = jo.getString("username");
        AuthenticationResponse authenticationResponse = userService.forgetPassword(username);
        Cookie cookie = CookieUtil.createCookie(JwtService.RESET_TOKEN, authenticationResponse.getToken(), 1000 * 5);
        response.addCookie(cookie);
        return ResponseHandler.generateResponse("Reset your account", HttpStatus.OK, null);
    }

    @PutMapping(value = "reset-password")
    public ResponseEntity<Object> resetPassword(@RequestBody String body, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jo = new JSONObject(body);
        String password = jo.getString("password");
        String token = CookieUtil.extractJwtToken(request, JwtService.RESET_TOKEN);
        long id = jwtService.extractId(token);
        userService.updateUserPassword(id, password);
        response.addCookie(CookieUtil.deleteCookie(JwtService.RESET_TOKEN));
        return ResponseHandler.generateResponse("Successful reset your account", HttpStatus.OK, null);
    }
}
