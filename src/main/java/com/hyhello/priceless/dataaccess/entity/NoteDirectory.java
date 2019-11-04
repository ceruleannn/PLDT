package com.hyhello.priceless.dataaccess.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 *
 */
@Entity
@Table(name = "tb_note_directory", schema = "hyhello", catalog = "")
public class NoteDirectory {

    private int id;
    private String title;
    private byte isFolder;
    private Byte expanded;
    private int noteId;
    private int parentId;
    private Timestamp createTime;
    private byte status;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "title", nullable = false, length = 255)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "isFolder", nullable = false)
    public byte getIsFolder() {
        return isFolder;
    }

    public void setIsFolder(byte isFolder) {
        this.isFolder = isFolder;
    }

    @Basic
    @Column(name = "expanded", nullable = true)
    public Byte getExpanded() {
        return expanded;
    }

    public void setExpanded(Byte expanded) {
        this.expanded = expanded;
    }

    @Basic
    @Column(name = "noteId", nullable = false)
    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    @Basic
    @Column(name = "parentId", nullable = false)
    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    @Basic
    @Column(name = "createTime", nullable = false)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "status", nullable = false)
    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteDirectory that = (NoteDirectory) o;
        return id == that.id &&
                isFolder == that.isFolder &&
                noteId == that.noteId &&
                parentId == that.parentId &&
                status == that.status &&
                Objects.equals(title, that.title) &&
                Objects.equals(expanded, that.expanded) &&
                Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, isFolder, expanded, noteId, parentId, createTime, status);
    }
}
