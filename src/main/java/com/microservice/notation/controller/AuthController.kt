package com.microservice.notation.controller
import com.microservice.notation.extensions.GenerateGuuid
import com.microservice.notation.models.ERole
import com.microservice.notation.models.Role
import com.microservice.notation.models.User
import com.microservice.notation.payload.request.LoginRequest
import com.microservice.notation.payload.request.SignupRequest
import com.microservice.notation.payload.response.JwtResponse
import com.microservice.notation.payload.response.MessageResponse
import com.microservice.notation.repository.RoleRepository
import com.microservice.notation.repository.UserRepository
import com.microservice.notation.security.jwt.JwtUtils
import com.microservice.notation.security.services.UserDetailsImpl
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = ["*"], maxAge = 3600)
class AuthController {

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Autowired
    lateinit var encoder: PasswordEncoder

    @Autowired
    lateinit var jwtUtils: JwtUtils

    @PostMapping("/signin")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<*> {

        val authentication = authenticationManager
                .authenticate(UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password))

        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtUtils.generateJwtToken(authentication)

        val userDetails = authentication.principal as UserDetailsImpl
        val roles = userDetails.authorities.parallelStream()
                .map(GrantedAuthority::getAuthority)
                .toList()

        return ResponseEntity.ok(
                JwtResponse(
                        jwt,
                        userDetails.id,
                        userDetails.username,
                        userDetails.email,
                        roles
                )
        )
    }

    @PostMapping("/signup")
    fun registerUser(@Valid @RequestBody signUpRequest: SignupRequest): ResponseEntity<*> {
        if (userRepository.existsByUsername(signUpRequest.username)) {
            return ResponseEntity.badRequest().body(MessageResponse("Username is already taken!"))
        }

        if (userRepository.existsByEmail(signUpRequest.email)) {
            return ResponseEntity.badRequest().body(MessageResponse("Email is already in use!"))
        }

        // Create new user's account
        val user = User(
                signUpRequest.username,
                signUpRequest.email,
                encoder.encode(signUpRequest.password)
        )
        val id: Long = GenerateGuuid.getRandomGuid()
        user.id = id

        val strRoles = signUpRequest.role
        val roles = HashSet<Role>()

        if (strRoles == null) {
            val userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow { RuntimeException("Error: Role is not found.") }
            roles.add(userRole)
        } else {
            strRoles.forEach { role ->
                when (role) {
                    "ROLE_ADMIN" -> {
                        val adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow { RuntimeException("Error: Role is not found.") }
                        roles.add(adminRole)
                    }
                    "ROLE_MODERATOR" -> {
                        val modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow { RuntimeException("Error: Role is not found.") }
                        roles.add(modRole)
                    }
                    else -> {
                        val userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow { RuntimeException("Error: Role is not found.") }
                        roles.add(userRole)
                    }
                }
            }
        }

        user.roles = roles
        userRepository.save(user)

        return ResponseEntity.ok(MessageResponse("User registered successfully!"))
    }
}
