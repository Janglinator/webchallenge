package demo.adapter.out.entity

import demo.core.data.User
import lombok.NoArgsConstructor
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
//@NoArgsConstructor
class UserEntity(
    @Id
    val id: String,
    @Column(nullable = false)
    val username: String,
    @Column(nullable = false)
    val passHash: String,
) {
    constructor(): this("", "", "")

    fun toDomainModel(): User {
        return User(id, username)
    }
}