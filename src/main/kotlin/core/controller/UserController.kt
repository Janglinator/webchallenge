package core.controller

import core.model.User
import core.model.UserRegistration
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import core.service.UserService

@RestController
@RequestMapping("api/v1/user")
class UserController(val userService: UserService) {
    @PostMapping("/register")
    fun registerUser(
        @RequestBody registration: UserRegistration
    ) {
        val existing = userService.findUser(registration.username)

        if (existing != null) {
            throw ResponseStatusException(
                HttpStatus.CONFLICT, "User already exists", null
            )
        }

        val user = User(null, registration.username, registration.password)
        userService.post(user)
    }

    @PostMapping("/changePassword")
    fun changePassword(
        @RequestHeader("Authorization") authorization: String,
        @RequestBody newPassword: String
    ) {
        val user = userService.validateUser(authorization)
        // TODO: Encryption
        user.password = newPassword
        userService.post(user)
    }

    @PostMapping("/changeUsername")
    fun changeUsername(
        @RequestHeader("Authorization") authorization: String,
        @RequestBody newUsername: String
    ) {
        val user = userService.validateUser(authorization)
        user.username = newUsername
        userService.post(user)
    }
}