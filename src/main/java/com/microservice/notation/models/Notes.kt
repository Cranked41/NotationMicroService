package com.microservice.notation.models

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "notes")
data class Notes(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "note_id") var noteId: Long,
        @JsonIgnore @Column(name = "note_guuid") val noteGuuid: String = UUID.randomUUID().toString(),
        @Column(nullable = false) val userGuuId: String,
        @Column(nullable = true) val subject: String? = null,
        @Column(name = "description") val description: String? = null,
        @Column(name = "createdTime") val createdTime: String = LocalDateTime.now().toString(),
        @Column(name = "deletedTime") val deletedTime: String? = null
)