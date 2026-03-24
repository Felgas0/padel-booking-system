package pt.isel.ls.API.dtosAPI.user

import kotlinx.serialization.Serializable

/**
 * Represents the response returned after a successful authentication (login).
 *
 * @property id the unique identifier of the authenticated user
 * @property token the authentication token associated with the user
 */
@Serializable
data class AuthResponse(
    val id: Int,
    val token: String
)
