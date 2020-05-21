package com.hyhello.priceless.controller;

import com.hyhello.priceless.dto.Node;
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

    @PutMapping
    @ResponseBody
    public Response update(@RequestBody List<Node> dir) throws IOException {
        return service.update(dir);
    }

    @DeleteMapping("/{nodeId}")
    @ResponseBody
    public Response delete(@PathVariable int nodeId) throws IOException {
        return service.deleteNode(nodeId);
    }

    @PostMapping
    @ResponseBody
    public Response add(Node node) throws IOException {
        return service.addNode(node);
    }

    @PutMapping("/rename")
    @ResponseBody
    public Response rename(Node node) throws IOException {
        return service.renameNode(node);
    }

    @GetMapping
    @ResponseBody
    public Response get(String dir) throws IOException {
        List<Node> result = service.get();
        return new NoteDirectoryResponse(200, "更新目录成功!", result);
    }

//    @PostMapping
//    @ResponseBody
//    public Response newFile() throws IOException {
//        return new Response(200, "更新目录成功!");
//    }

}
