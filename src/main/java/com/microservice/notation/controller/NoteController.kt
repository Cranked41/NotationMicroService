package com.microservice.notation.controller

import com.microservice.notation.models.Notes
import com.microservice.notation.payload.response.MessageResponse
import com.microservice.notation.service.NoteService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/notes/")
class NoteController {
    @Autowired
    private lateinit var noteService: NoteService

    @PostMapping("saveNote")
    fun saveNote(@Valid @RequestBody notes: Notes): ResponseEntity<*> {
        noteService.saveNote(notes)
        return ResponseEntity.ok(MessageResponse("ok"))
    }

    @DeleteMapping("deleteNote")
    fun deleteNote(@Valid @RequestBody noteId: String): ResponseEntity<*> {
        noteService.deleteNote(noteId)
        return ResponseEntity.ok(MessageResponse("Not Silindi"))
    }

    @GetMapping("getNotesByUserId")
    fun getUserNotesByUserId(@Valid @RequestBody userId: String): ResponseEntity<*> {
        return ResponseEntity.ok(noteService.findNotesByUserId(userId = userId))
    }
}