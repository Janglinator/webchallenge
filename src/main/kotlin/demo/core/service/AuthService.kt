package demo.core.service

import demo.core.data.AuthRequest
import demo.core.data.User
import demo.core.util.exception.AuthenticationFailedException
import demo.core.util.exception.UnauthorizedException
import demo.core.util.exception.UserAlreadyExistsException
import demo.port.`in`.AuthUseCase
import demo.port.out.AuthPort
import demo.port.out.UserCRUDPort
import org.springframework.stereotype.Component

@Component
class AuthService(
    val authPort: AuthPort,
): AuthUseCase {
    private var authenticatedUser: User? = null

    override fun register(request: AuthRequest) {
        authPort.loadUser(request.username)?.let {
            throw UserAlreadyExistsException()
        }

        authPort.registerUser(request)
    }

    override fun authenticate(request: AuthRequest): String {
        authPort.validateCredentials(request)
        val user = authPort.loadUser(request.username)
        val token = authPort.setAuthenticationContext(request)
        authenticatedUser = user
        return token
    }

    override fun getUser(): User {
        return authenticatedUser ?: throw UnauthorizedException("User is not authenticated")
    }
}

