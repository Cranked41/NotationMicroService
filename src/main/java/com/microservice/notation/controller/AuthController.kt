package com.microservice.notation.controller

import com.microservice.notation.models.ERole
import com.microservice.notation.models.Role
import com.microservice.notation.models.User
import com.microservice.notation.payload.request.LoginRequestModel
import com.microservice.notation.payload.request.SignupRequestModel
import com.microservice.notation.payload.response.JwtResponse
import com.microservice.notation.payload.response.ResponseModel
import com.microservice.notation.repository.RoleRepository
import com.microservice.notation.repository.UserRepository
import com.microservice.notation.security.jwt.JwtUtils
import com.microservice.notation.security.services.UserDetailsImpl
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.collections.HashSet


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = ["*"], maxAge = 3600)
class AuthController {
    @Autowired
    lateinit var messageSource: MessageSource

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
    fun authenticateUser(@Valid @RequestBody loginRequestModel: LoginRequestModel): ResponseEntity<*> {

        val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(loginRequestModel.username, loginRequestModel.password))

        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtUtils.generateJwtToken(authentication)

        val userDetails = authentication.principal as UserDetailsImpl
        val roles = userDetails.authorities.parallelStream().map(GrantedAuthority::getAuthority).toList()

        return ResponseEntity.ok(JwtResponse(jwt, userDetails.id, userDetails.username, userDetails.email, roles))
    }

    @PostMapping("/signup")
    fun registerUser(@RequestHeader(name = "Accept-Language", required = false) @Valid @RequestBody signUpRequestModel: SignupRequestModel): ResponseEntity<*> {


        if (userRepository.existsByUsername(signUpRequestModel.username)) {
            return ResponseEntity.badRequest().body(ResponseModel(success = false, code = HttpStatus.BAD_REQUEST.value(), message = getMessage("UsernameAlreadyTaken"), data = null))
        }

        if (userRepository.existsByEmail(signUpRequestModel.email)) {
            return ResponseEntity.badRequest().body(ResponseModel(success = false, code = HttpStatus.BAD_REQUEST.value(), message = getMessage("EmailAlreadyTaken"), data = null))
        }


        // Create new user's account
        val user = User(username = signUpRequestModel.username, userGuuId = UUID.randomUUID().toString(), email = signUpRequestModel.email, password = encoder.encode(signUpRequestModel.password), name = signUpRequestModel.name, surname = signUpRequestModel.surname, phoneNumber = signUpRequestModel.phoneNumber, birthDate = signUpRequestModel.birthDate)
        val strRoles = signUpRequestModel.role
        val roles = HashSet<Role>()

        if (strRoles == null) {
            val userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow { RuntimeException("Error: Role is not found.") }
            roles.add(userRole)
        } else {
            strRoles.forEach { role ->
                when (role) {
                    "ROLE_ADMIN" -> {
                        val adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow { RuntimeException("Error: Role is not found.") }
                        roles.add(adminRole)
                    }

                    "ROLE_MODERATOR" -> {
                        val modRole = roleRepository.findByName(ERole.ROLE_MODERATOR).orElseThrow { RuntimeException("Error: Role is not found.") }
                        roles.add(modRole)
                    }

                    else -> {
                        val userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow { RuntimeException("Error: Role is not found.") }
                        roles.add(userRole)
                    }
                }
            }
        }

        user.roles = roles
        userRepository.save(user)
        return ResponseEntity.ok(ResponseModel(success = true, code = HttpStatus.OK.value(), message = getMessage("UserRegisteredSuccessfully"), data = user.userGuuId))
    }

    @GetMapping("getUserInfo")
    fun getUserInfo(@Valid @RequestParam userId: String): ResponseEntity<*> {
        val user = userRepository.findAll().filter { it?.userGuuId == userId }.first()
        return ResponseEntity.ok(ResponseModel.success(code = HttpStatus.OK.value(), data = user))
    }

    fun getMessage(translationKey: String): String {
        return messageSource.getMessage(translationKey, null, LocaleContextHolder.getLocale())
    }
}
