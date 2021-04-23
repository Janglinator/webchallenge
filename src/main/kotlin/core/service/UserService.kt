package core.service

import core.model.User
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.nio.charset.StandardCharsets
import java.util.*

@Repository
interface UserRepository : CrudRepository<User, String> {
    @Query("select * from user")
    fun findUsers(): List<User>
}

@Service
class UserService(val db: UserRepository) {
    // TODO: Use spring boot's security library.  It's too complex for a hack-n-slash demo like this.
    fun validateUser(authorization: String): User {
        var username: String? = null
        var password: String? = null

        if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
            val base64Credentials = authorization.substring("Basic".length).trim()
            val credDecoded: ByteArray = Base64.getDecoder().decode(base64Credentials)
            val credentials = String(credDecoded, StandardCharsets.UTF_8)
            val values = credentials.split(":")
            username = values[0]
            password = values[1]
        }

        var user = findUser(username ?: "")

        if (user == null || user?.password != password) {
            throw ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "User not found", null
            )
        }

        return user!!
    }

    fun findUser(userName: String): User? {
        return db.findUsers().firstOrNull { it.username == userName }
    }

    fun post(user: User) {
        db.save(user)
    }
}