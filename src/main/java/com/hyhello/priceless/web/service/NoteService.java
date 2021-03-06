package com.hyhello.priceless.web.service;

import com.hyhello.priceless.dataaccess.entity.Note;
import com.hyhello.priceless.dataaccess.repository.NoteDirectoryRepository;
import com.hyhello.priceless.dataaccess.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 */
@Service
public class NoteService {

    private final NoteRepository repository;

    @Autowired
    public NoteService(NoteRepository repository) {
        this.repository = repository;
    }

    public Note createNote(){
        Note note = new Note();
        note.setUid(1);
        note.setVersion(1);
        note.setText("");
        note.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        note.setNoteId(repository.getMaxNoteId() + 1);
        return repository.save(note);
    }

    public void deleteNote(int nid){
        repository.deleteById(nid);
    }

    public void updateNote(int noteId, String text){
        Note note = new Note();
        note.setUid(1);
        int version = repository.getMaxVersion(noteId) + 1;
        note.setVersion(version);
        note.setText(text);
        note.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        note.setNoteId(noteId);
        repository.save(note);
    }

    public Note getNote(int noteId){
        return repository.findFirstByNoteIdOrderByVersionDesc(noteId);
    }
}
