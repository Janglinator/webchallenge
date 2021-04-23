import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("USER")
data class User (
    @Id
    private val id: String,
    private val username: String,
    private val passwordHash: String
//    private val password: String? = null // standard getters and setters
)