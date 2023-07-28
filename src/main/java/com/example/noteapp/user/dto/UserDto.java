package com.example.noteapp.user.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String name;
    private java.util.Date createdAt;
    private java.util.Date updatedAt;
}
