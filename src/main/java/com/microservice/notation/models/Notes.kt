package com.microservice.notation.models

import jakarta.persistence.*

@Entity
@Table(name = "notes")
data class Notes(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val noteId: Long,

        @Column(nullable = false)
        val userId: String,

        @Column(nullable = false)
        val email: String,

        @Column(nullable = true)
        val subject: String? = null,

        @Column(name = "description")
        val description: String? = null
)