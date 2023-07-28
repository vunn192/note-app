package com.example.noteapp.web;

import com.example.noteapp.common.util.CookieUtil;
import com.example.noteapp.common.jwt.JwtService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WebController {
    @GetMapping(value = "/sign-up")
    public String signUp() {
        return "signUp";
    }

    @GetMapping(value = {"/", "sign-in"})
    public String index(HttpServletRequest request){
        if (!CookieUtil.isLogged(request)) {
            return "signIn";
        }

        return "dashboard";
    }

    @GetMapping(value = "/dashboard")
    public String dashboard(){
        return "dashboard";
    }

    @GetMapping(value = "/profile")
    public String profile(){
        return "profile";
    }

    @GetMapping(value = "/forget-password")
    public String forgetPassword(){
        return "forgetPassword";
    }

    @GetMapping(value = "/reset-password")
    public String resetPassword(){
        return "resetPassword";
    }
}
