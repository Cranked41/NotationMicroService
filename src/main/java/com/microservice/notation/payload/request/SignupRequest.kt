package com.microservice.notation.payload.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class SignupRequest(val username: @NotBlank @Size(min = 3, max = 20) String,
                         val email: @NotBlank @Size(max = 50) @Email String,
                         var role: Set<String>? = null,
                         val password: @NotBlank @Size(min = 6, max = 40) String)
