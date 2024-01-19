package com.microservice.notation.repository

import com.microservice.notation.models.Notes
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface INoteRepository : JpaRepository<Notes, Long> {
}