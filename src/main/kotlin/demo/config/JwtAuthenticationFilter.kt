package demo.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(authenticationManager: AuthenticationManager) :
    UsernamePasswordAuthenticationFilter(authenticationManager) {

    override fun attemptAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): Authentication {
        val username = request.getParameter("username")
        val password = request.getParameter("password")
        val authRequest = UsernamePasswordAuthenticationToken(username, password)
        return authenticationManager.authenticate(authRequest)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain?,
        authResult: Authentication
    ) {
        val userDetails = authResult.principal as User
        val claims: Claims = Jwts.claims().setSubject(userDetails.username)

        val token = Jwts.builder()
            .setClaims(claims)
            .setExpiration(Date(System.currentTimeMillis() + TOKEN_VALIDITY_MS))
            .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
            .compact()

        response.addHeader(AUTHORIZATION_HEADER, TOKEN_PREFIX + token)
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        failed: AuthenticationException
    ) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, failed.message)
    }

    companion object {
        const val TOKEN_VALIDITY_MS: Long = 3_600_000
        const val SECRET_KEY = "urnevergonnaguessthis"
        const val AUTHORIZATION_HEADER = "Authorization"
        const val TOKEN_PREFIX = "Bearer "
    }
}
