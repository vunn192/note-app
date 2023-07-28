package com.example.noteapp.note.service;

import com.example.noteapp.note.dto.NoteCreationDto;
import com.example.noteapp.note.model.Note;
import com.example.noteapp.note.reposity.NoteReposity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final ModelMapper modelMapper;
    private final NoteReposity noteReposity;
    public Page<Note> findAllByUserIdAndName(long id, String name, Pageable page) {
        return noteReposity.findAllByUserIdAndName(id, name, page);
    }
    public Note createNoteByUserId(long id, NoteCreationDto noteCreationDto) {
        Note note = modelMapper.map(noteCreationDto, Note.class);
        note.setUserId(id);
        note.setCreatedAt(new Date(System.currentTimeMillis()));
        note.setUpdatedAt(new Date(System.currentTimeMillis()));
        noteReposity.save(note);
        return note;
    }
    public void deleteNote(long id) {
        noteReposity.deleteById(id);
    }
}
