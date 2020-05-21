package com.hyhello.priceless.dto.resp;

import com.hyhello.priceless.dto.Node;
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

    protected List<Node> directory;

    public NoteDirectoryResponse(int code, String msg, List<Node> directory){
        super(code, msg);
        this.directory = directory;
    }

    public NoteDirectoryResponse(Response response, List<Node> directory){
        super(response.code, response.msg);
        this.directory = directory;
    }
}

