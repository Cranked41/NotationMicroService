package com.microservice.notation.controller

import com.microservice.notation.models.Notes
import com.microservice.notation.payload.response.ResponseModel
import com.microservice.notation.service.NoteService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/notes/")
class NoteController {
    @Autowired
    private lateinit var noteService: NoteService


    @PostMapping("saveNote")
    fun saveNote(@Valid @RequestBody notes: Notes): ResponseEntity<*> {
        noteService.saveNote(notes)
        return ResponseEntity.ok(ResponseModel.success(code = HttpStatus.OK.value(), data = true))

    }

    @DeleteMapping("deleteNote")
    fun deleteNote(@Valid @RequestBody noteId: String): ResponseEntity<*> {
        noteService.deleteNote(noteId)
        return ResponseEntity.ok(ResponseModel.success(code = HttpStatus.OK.value(), "Not Silindi"))
    }

    @GetMapping("getNotesByUserId/{userId}")
    fun getUserNotesByUserId(@Valid @PathVariable userId: String): ResponseEntity<*> {
        return ResponseEntity.ok(ResponseModel.success(code = HttpStatus.OK.value(), data = noteService.findNotesByUserId(userId = userId)))
    }
}