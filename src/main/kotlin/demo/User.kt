import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("USERS")
data class User (
    @Id
    val id: String,
    val username: String,
    val passwordHash: String
)