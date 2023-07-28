package com.example.noteapp.note.dto;


import lombok.*;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoteDto {
    private long id;
    private String name;
    private String content;
}
