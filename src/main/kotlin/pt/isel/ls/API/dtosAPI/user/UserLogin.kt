package pt.isel.ls.API.dtosAPI.user

import kotlinx.serialization.Serializable

/**
 * Represents a user login request.
 *
 * @property name name of the user
 * @property password password of the user
 */
@Serializable
data class UserLogin(
    val name: String,
    val password: String
)