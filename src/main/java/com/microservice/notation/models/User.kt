package com.microservice.notation.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.microservice.notation.extensions.GenerateGuuid
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(columnNames = ["username"]), UniqueConstraint(columnNames = ["email"])])
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = GenerateGuuid.getRandomGuid(),
        var userGuuId: String? = null,
        var name: String? = null,
        var surname: String? = null,
        var phoneNumber: Int? = null,
        var username: @NotBlank @Size(max = 20) String? = null,
        var email: @NotBlank @Size(max = 50) @Email String? = null,
        @JsonIgnore var password: @NotBlank @Size(max = 120) String? = null,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        var birthDate: LocalDate? = null,

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "user_roles", joinColumns = [JoinColumn(name = "user_id")], inverseJoinColumns = [JoinColumn(name = "role_id")])
        var roles: Set<Role> = HashSet()
)

