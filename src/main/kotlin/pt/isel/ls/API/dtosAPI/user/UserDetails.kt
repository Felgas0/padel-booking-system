package pt.isel.ls.API.dtosAPI.user

import kotlinx.serialization.Serializable
import pt.isel.ls.domain.Email

/**
 * Represents detailed information about a user.
 *
 * @property id the unique identifier of the user
 * @property name the name of the user
 * @property email the email address of the user
 */
@Serializable
class UserDetails(
    val id: Int,
    val name: String,
    val email: Email,
)
