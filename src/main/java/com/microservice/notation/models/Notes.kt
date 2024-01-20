package com.microservice.notation.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import jakarta.persistence.*
import lombok.extern.apachecommons.CommonsLog
import java.util.UUID

@Entity
@Table(name = "notes")
data class Notes(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "note_id") var noteId: Long,
        @JsonIgnore @Column(name = "note_guuid") val NoteGuuid: String = UUID.randomUUID().toString(), @Column(nullable = false) val userId: String,
        @Column(nullable = false) val email: String,
        @Column(nullable = true) val subject: String? = null,
        @Column(name = "description") val description: String? = null)