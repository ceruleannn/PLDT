package com.hyhello.priceless.dataaccess.repository;

import com.hyhello.priceless.dataaccess.entity.NoteDirectory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

/**
 *
 */
@Repository
public interface NoteDirectoryRepository extends JpaRepository<NoteDirectory, Integer> {
    List<NoteDirectory> findNoteDirectoriesByParentIdEquals(int parentId);
}
