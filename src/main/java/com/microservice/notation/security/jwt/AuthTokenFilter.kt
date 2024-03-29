package com.microservice.notation.security.jwt

import com.microservice.notation.security.services.UserDetailsServiceImpl
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.util.StringUtils
import org.springframework.web.client.HttpClientErrorException.Unauthorized
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

class AuthTokenFilter : OncePerRequestFilter() {
    @Autowired
    private val jwtUtils: JwtUtils? = null


    @Autowired
    private val userDetailsService: UserDetailsServiceImpl? = null

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            if (!request.requestURL.contains("/api/auth")) {
                val jwt = parseJwt(request)
                if (jwt != null && jwtUtils!!.validateJwtToken(jwt)) {
                    val username = jwtUtils.getUserNameFromJwtToken(jwt)
                    val userDetails = userDetailsService!!.loadUserByUsername(username)
                    val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails?.authorities)
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authentication
                } else {
                    throw MalformedJwtException("malformed")
                }
            }
            filterChain.doFilter(request, response)
        } catch (e: ExpiredJwtException) {
            // Token geçerli değilse veya süresi dolmuşsa UnauthorizedException fırlatılır
            SecurityContextHolder.clearContext()
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
        } catch (e: SignatureException) {
            SecurityContextHolder.clearContext()
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Yetkisiz erişim")
        } catch (e: MalformedJwtException) {
            SecurityContextHolder.clearContext()
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Yetkisiz erişim tespit edildi")
        } catch (e: Exception) {
            Companion.logger.error("Cannot set user authentication: {}", e)
        }
    }

    private fun parseJwt(request: HttpServletRequest): String? {
        val headerAuth = request.getHeader("Authorization")
        return if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            headerAuth.substringAfter("Bearer ")
        } else null
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AuthTokenFilter::class.java)
    }
}
