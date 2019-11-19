package auth

import io.ktor.auth.Principal

sealed class BackendUser : Principal {
    data class AdminUser(val name: String) : BackendUser()
    data class Guest(val scope : List<String>) : BackendUser()
}