
package com.example.noteapp.note.reposity;


import com.example.noteapp.note.model.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface NoteReposity extends JpaRepository<Note, Long> {
    @Query("select u from Note u where u.userId = :id and u.name like LOWER(concat('%',:name,'%'))")
    Page<Note> findAllByUserIdAndName(long id, String name, Pageable pageable);
}
