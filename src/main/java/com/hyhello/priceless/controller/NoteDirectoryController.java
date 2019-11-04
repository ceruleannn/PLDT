package com.hyhello.priceless.controller;

import com.hyhello.priceless.dto.DirectoryNode;
import com.hyhello.priceless.dto.resp.NoteDirectoryResponse;
import com.hyhello.priceless.dto.resp.Response;
import com.hyhello.priceless.service.NoteDirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 *
 */
@RestController
@RequestMapping("/note/directory")
public class NoteDirectoryController {

    private final NoteDirectoryService service;

    @Autowired
    public NoteDirectoryController(NoteDirectoryService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseBody
    public Response update(@RequestBody List<DirectoryNode> dir) throws IOException {
        service.update(dir);
        return new Response(200, "更新目录成功!");
    }

    @GetMapping
    @ResponseBody
    public Response get(String dir) throws IOException {
        List<DirectoryNode> result = service.get();
        return new NoteDirectoryResponse(200, "更新目录成功!", result);
    }

    @PostMapping
    @ResponseBody
    public Response newFile() throws IOException {
        return new Response(200, "更新目录成功!");
    }

}
