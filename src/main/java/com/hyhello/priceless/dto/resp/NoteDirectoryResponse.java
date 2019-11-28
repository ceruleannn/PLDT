package com.hyhello.priceless.dto.resp;

import com.hyhello.priceless.dto.DirectoryNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 */
@Getter
@Setter
public class NoteDirectoryResponse extends Response{
    protected List<DirectoryNode> directory;

    public NoteDirectoryResponse(int code, String msg, List<DirectoryNode> directory){
        super(code, msg);
        this.directory = directory;
    }
}

