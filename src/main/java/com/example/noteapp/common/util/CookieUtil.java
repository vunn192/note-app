package com.example.noteapp.common.util;

import com.example.noteapp.common.jwt.JwtService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil {
    private static final String DOMAIN = "localhost";
    private static final String PATH = "/";
    private static final int AGE = 60 * 60;

    public static String extractJwtToken(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for(Cookie cookie : cookies) {
                if (name.equals(cookie.getName())){
                    return cookie.getValue();
                }
                break;
            }
        }

        return null;
    }
    public static boolean isLogged(HttpServletRequest request) {
        String token = extractJwtToken(request, JwtService.AUTH_TOKEN);
        return (token != null);
    }
    public static Cookie createCookie(String name, String token) {
        Cookie cookie = new Cookie(name, token);
        cookie.setMaxAge(AGE);
        cookie.setPath(PATH);
        cookie.setDomain(DOMAIN);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        return cookie;
    }
    public static Cookie createCookie(String name, String token, int age) {
        Cookie cookie = new Cookie(name, token);
        cookie.setMaxAge(age);
        cookie.setPath(PATH);
        cookie.setDomain(DOMAIN);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        return cookie;
    }
    public static Cookie deleteCookie(String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath(PATH);
        cookie.setDomain(DOMAIN);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        return cookie;
    }
}
