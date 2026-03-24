package pt.isel.ls.API.dtosAPI.user

import kotlinx.serialization.Serializable

/**
 * Represents a user creation request.
 *
 * @property name name of the user
 * @property email email of the user
 * @property password password of the user
 */
@Serializable
data class UserCreate(val name: String, val email: String, val password: String)
