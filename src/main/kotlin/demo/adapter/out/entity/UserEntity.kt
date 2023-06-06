package demo.adapter.out.entity

import demo.core.data.User
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: String,
    @Column(nullable = false)
    val username: String,
    @Column(nullable = false)
    val passHash: String,
) {
    fun toDomainModel(): User {
        return User(id, username)
    }
}