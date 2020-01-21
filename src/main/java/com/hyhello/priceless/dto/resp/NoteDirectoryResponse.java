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
    public static final Response SUCCESS = new Response(200, "success");
    public static final Response NOT_EXIST_NODE = new Response(500, "node not exist");
    public static final Response REMOVE_NOT_EMPTY_FOLDER = new Response(501, "can not remove a not empty folder");

    protected List<DirectoryNode> directory;

    public NoteDirectoryResponse(int code, String msg, List<DirectoryNode> directory){
        super(code, msg);
        this.directory = directory;
    }
}

