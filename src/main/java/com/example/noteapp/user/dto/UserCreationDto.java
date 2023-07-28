package com.example.noteapp.user.dto;

import lombok.*;

import javax.persistence.Column;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationDto {
    private String name;
    private String username;
    private String password;
}

