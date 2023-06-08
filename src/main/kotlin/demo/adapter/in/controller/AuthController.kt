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
    fun registerUser(@RequestBody request: AuthRequest): Any {
        try {
            authUseCase.register(request)
            return ResponseEntity.ok(null)
        } catch (ex: Exception) {
            // TODO: Pass back exception
            return ResponseEntity.badRequest()
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequest): Any {
        try {
            val token = authUseCase.authenticate(request)
            return ResponseEntity.ok(TokenResponse(token))
        } catch (ex: Exception) {
            // TODO: Pass back exception
            return ResponseEntity.badRequest()
        }
    }

    @GetMapping("/user")
    fun getCurrentUser(): Any {
        val authentication = SecurityContextHolder.getContext().authentication
        authentication?.let {
            return ResponseEntity.ok(authentication.toString())
        }
        // TODO: Should be unauthorized
        return ResponseEntity.badRequest()
    }
}

data class TokenResponse(val token: String)