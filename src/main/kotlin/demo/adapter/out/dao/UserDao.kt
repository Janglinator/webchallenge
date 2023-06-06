package demo.adapter.out.dao

import demo.adapter.out.repository.UserRepository
import demo.port.out.UserCRUDPort
import org.springframework.stereotype.Component

@Component
class UserDao(
    val userRepository: UserRepository,
): UserCRUDPort {

}