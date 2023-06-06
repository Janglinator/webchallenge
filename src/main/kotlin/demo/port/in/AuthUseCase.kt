package demo.port.`in`

import demo.core.data.AuthRequest
import demo.core.data.User

interface AuthUseCase {
    fun register(request: AuthRequest)
    fun authenticate(request: AuthRequest): String
    fun getUser(): User?
}