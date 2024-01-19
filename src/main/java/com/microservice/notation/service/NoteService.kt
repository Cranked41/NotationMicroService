package com.microservice.notation.service


import com.microservice.notation.models.Notes
import com.microservice.notation.repository.INoteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class NoteService : INoteService {

    @Autowired
    private lateinit var noteRepository: INoteRepository
    override fun saveNote(notes: Notes) {
        noteRepository.save(notes)
    }

    override fun deleteNote(noteId: String) {
        noteRepository.findAll().parallelStream().dropWhile { it.noteId.toString() == noteId }

    }

    override fun findNotesByUserId(userId: String): MutableList<Notes> {
        return noteRepository.findAll().parallelStream().filter { it.userId == userId }.toList()
    }

    override fun findNotesByEmail(email: String): MutableList<Notes> {
        return noteRepository.findAll().parallelStream().filter { it.email == email }.toList()
    }

}