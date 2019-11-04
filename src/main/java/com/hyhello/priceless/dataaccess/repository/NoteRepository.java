package com.hyhello.priceless.dataaccess.repository;

import com.hyhello.priceless.dataaccess.entity.Note;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 *
 */
@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {

    @Query("select max(noteId) from Note")
    public int getMaxNoteId();

    @Query("select max(version) from Note where noteId = :noteId")
    public int getMaxVersion(@Param("noteId") int noteId);

    public Note findByNoteIdOrderByVersionDesc(int noteId, Pageable pageable);
}
