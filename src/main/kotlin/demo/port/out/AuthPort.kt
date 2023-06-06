package demo.port.out

import demo.core.data.AuthRequest
import demo.core.data.User

interface AuthPort {
    fun loadUser(username: String): User?
    fun registerUser(request: AuthRequest)
    fun validateCredentials(request: AuthRequest)
    fun setAuthenticationContext(request: AuthRequest): String
}