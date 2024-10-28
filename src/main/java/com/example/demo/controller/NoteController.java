package com.example.demo.controller;

import com.example.demo.entity.NoteEntity;
import com.example.demo.repository.NoteRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notes")
public class NoteController {

    private final NoteRepository noteRepository;

    @GetMapping
    public List<NoteEntity> getAllNotes(@PageableDefault Pageable pageable) {
        return noteRepository.findAll(pageable).getContent();
    }

    @GetMapping("/{id}")
    public NoteEntity getTenantById(@PathVariable Long id) {
        return noteRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    @PostMapping
    public NoteEntity createTenant(
        @RequestParam(name = "title") String title,
        @RequestParam(name = "content") String content
    ) {
        return noteRepository.save(new NoteEntity(title, content));
    }

    @PutMapping("/{id}")
    public NoteEntity updateTenant(
        @PathVariable Long id,
        @RequestParam(name = "title") String title,
        @RequestParam(name = "content") String content
    ) {
        NoteEntity note = noteRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        note.changeTitle(title);
        note.changeContent(content);
        return noteRepository.save(note);
    }
    
    @DeleteMapping("/{id}")
    public void deleteTenant(@PathVariable Long id) {
        noteRepository.deleteById(id);
    }
}
