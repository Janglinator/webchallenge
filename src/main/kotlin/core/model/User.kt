package core.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("USER")
data class User (
    @Id val id: String?,
    var username: String?,
    var password: String?
)

data class UserRegistration (
    val username: String,
    val password: String
)