package com.microservice.notation.service

import com.microservice.notation.models.Notes


interface INoteService {

    fun saveNote(notes: Notes)
    fun deleteNote(noteId: String)
    fun findNotesByUserId(userName: String):MutableList<Notes>
    fun findNotesByEmail(email: String):MutableList<Notes>
}