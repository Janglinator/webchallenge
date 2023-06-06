package demo.adapter.`in`.controller

import demo.core.data.AuthRequest
import demo.port.`in`.AuthUseCase
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    val authUseCase: AuthUseCase,
) {
    @PostMapping("/register")
    fun registerUser(@RequestBody request: AuthRequest): ResponseEntity<*> {
        authUseCase.register(request)
        return ResponseEntity.ok(null)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequest): ResponseEntity<*> {
        val token = authUseCase.authenticate(request)
        return ResponseEntity.ok(TokenResponse(token))
    }

    @GetMapping("/user")
    fun getCurrentUser(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication.name
    }
}

data class TokenResponse(val token: String)