package com.example.noteapp.auth;


import com.example.noteapp.auth.model.AuthenticationRequest;
import com.example.noteapp.auth.model.AuthenticationResponse;
import com.example.noteapp.auth.service.AuthenticationService;
import com.example.noteapp.common.util.CookieUtil;
import com.example.noteapp.common.ResponseHandler;
import com.example.noteapp.common.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<Object> authentication(@RequestBody AuthenticationRequest request,
                                                 HttpServletResponse response) {

        AuthenticationResponse authenticationResponse = authenticationService.authentication(request);
        Cookie cookie = CookieUtil.createCookie(JwtService.AUTH_TOKEN, authenticationResponse.getToken());
        response.addCookie(cookie);
        return ResponseHandler.generateResponse("Successful login", HttpStatus.OK, null);
    }

    @DeleteMapping("/sign-out")
    public ResponseEntity<Object> logout(HttpServletResponse response) {

        response.addCookie(CookieUtil.deleteCookie(JwtService.AUTH_TOKEN));
        return ResponseHandler.generateResponse("Successful logout", HttpStatus.OK, null);
    }
}
