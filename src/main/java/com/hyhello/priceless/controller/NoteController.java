package com.hyhello.priceless.controller;

import com.hyhello.priceless.dataaccess.entity.Note;
import com.hyhello.priceless.dto.DirectoryNode;
import com.hyhello.priceless.dto.resp.NoteResponse;
import com.hyhello.priceless.dto.resp.Response;
import com.hyhello.priceless.service.NoteDirectoryService;
import com.hyhello.priceless.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 *
 */
@RestController
@RequestMapping("/note")
public class NoteController {

    private final NoteService service;

    @Autowired
    public NoteController(NoteService service) {
        this.service = service;
    }

    @GetMapping("/{noteId}")
    @ResponseBody
    public Response getNote(@PathVariable int noteId) throws IOException {
        Note note = service.getNote(noteId);
        return new NoteResponse(note);
    }



}
