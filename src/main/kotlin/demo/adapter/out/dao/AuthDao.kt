package demo.adapter.out.dao

import demo.adapter.out.entity.UserEntity
import demo.adapter.out.repository.UserRepository
import demo.core.data.AuthRequest
import demo.core.util.exception.AuthenticationFailedException
import demo.core.util.exception.UserAlreadyExistsException
import demo.port.out.AuthPort
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.util.*

@Component
class AuthDao(
    val userRepository: UserRepository,
    val authenticationManager: AuthenticationManager,
    val passwordEncoder: PasswordEncoder,
): AuthPort {
    override fun loadUser(username: String): demo.core.data.User? {
        loadUserEntity(username)?.let {
            return demo.core.data.User(it.id, it.username)
        }

        return null
    }

    fun loadUserEntity(username: String): UserEntity? {
        return userRepository.findByUsername(username)
    }

    override fun registerUser(request: AuthRequest) {
        val encodedPassword = passwordEncoder.encode(request.password)
        val user = UserEntity(UUID.randomUUID().toString(), request.username, encodedPassword)
        userRepository.save(user)
    }

    override fun validateCredentials(request: AuthRequest) {
        val entity = loadUserEntity(request.username) ?: throw AuthenticationFailedException()
        if (!passwordEncoder.matches(request.password, entity.passHash)) {
            throw AuthenticationFailedException()
        }
    }

    override fun setAuthenticationContext(request: AuthRequest): String {
        val user = User(request.username, request.password, emptyList())
        val auth = UsernamePasswordAuthenticationToken(user, null, user.authorities)
        val token = authenticationManager.authenticate(auth)
        SecurityContextHolder.getContext().authentication = token
        return token.toString()
    }
}