package com.microservice.notation.repository

import com.microservice.notation.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User?, Long?> {
    fun findByUsername(username: String): Optional<User>?
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
}
