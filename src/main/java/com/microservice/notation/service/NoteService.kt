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

    override fun deleteNote(noteGuuId: String) {
        noteRepository.deleteAll(noteRepository.findAll().parallelStream().filter { it.noteGuuid == noteGuuId }.toList())

    }

    override fun findNotesByUserId(userId: String): MutableList<Notes> {
        return noteRepository.findAll().parallelStream().filter { it.userGuuId == userId }.toList()
    }

}