package demo.config

import demo.adapter.out.entity.UserEntity
import demo.adapter.out.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class UserDetailsServiceImpl(
    val userRepository: UserRepository,
): UserDetailsService {
    override fun loadUserByUsername(username: String): User {
        val entity = loadUserEntity(username) ?: throw UsernameNotFoundException("User not found: $username")
        return User(entity.username, entity.passHash, emptyList())
    }

    fun loadUserEntity(username: String): UserEntity? {
        return userRepository.findByUsername(username)
    }
}