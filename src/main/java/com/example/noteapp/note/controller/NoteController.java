package com.example.noteapp.note.controller;

import com.example.noteapp.common.util.CookieUtil;
import com.example.noteapp.common.ResponseHandler;
import com.example.noteapp.common.jwt.JwtService;
import com.example.noteapp.note.dto.NoteCreationDto;
import com.example.noteapp.note.dto.NoteDto;
import com.example.noteapp.note.model.Note;
import com.example.noteapp.note.model.PageNote;
import com.example.noteapp.note.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/notes")
public class NoteController {
    private final NoteService noteService;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    @GetMapping("")
    public ResponseEntity<?> getNotes(HttpServletRequest request, @RequestParam(required = false) Optional<String> search,
                                      @RequestParam int page, @RequestParam int limit) {
        String token = CookieUtil.extractJwtToken(request, JwtService.AUTH_TOKEN);
        long id = jwtService.extractId(token);
        Pageable pageable = PageRequest.of(page, limit);
        String searchValue = search.orElse("");
        Page<Note> servicePage = noteService.findAllByUserIdAndName(id, searchValue, pageable);
        PageNote pageNote = new PageNote(servicePage.getTotalElements(), servicePage.getContent());
        return ResponseHandler.generateResponse("Success", HttpStatus.OK, pageNote);
    }

    @PostMapping("")
    public ResponseEntity<Object> createNote(@RequestBody NoteCreationDto noteCreationDto, HttpServletRequest request) {
        String token = CookieUtil.extractJwtToken(request, JwtService.AUTH_TOKEN);
        long id = jwtService.extractId(token);
        Note note = noteService.createNoteByUserId(id, noteCreationDto);
        NoteDto noteDto = modelMapper.map(note, NoteDto.class);
        return ResponseHandler.generateResponse("Success", HttpStatus.CREATED, noteDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteNote(@PathVariable long id) {
        noteService.deleteNote(id);
        return ResponseHandler.generateResponse("Success", HttpStatus.OK, null);
    }
}
