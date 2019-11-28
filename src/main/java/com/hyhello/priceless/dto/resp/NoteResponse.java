package com.hyhello.priceless.dto.resp;

import com.hyhello.priceless.dataaccess.entity.Note;
import lombok.Getter;
import lombok.Setter;

/**
 *
 */
@Getter
@Setter
public class NoteResponse extends Response{
    protected Note note;

    public NoteResponse(Note note, int code, String msg){
        this.note = note;
        this.code = code;
        this.msg = msg;
    }

    public NoteResponse(Note note){
        this.note = note;
    }
}
