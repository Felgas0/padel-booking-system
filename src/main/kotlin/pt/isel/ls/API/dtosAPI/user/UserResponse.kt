package pt.isel.ls.API.dtosAPI.user

import kotlinx.serialization.Serializable
import pt.isel.ls.utils.UUIDSerializer
import java.util.UUID

/**
 * Represents the response returned after creating a user.
 *
 * @property userId the unique identifier of the newly created user
 * @property token the authentication token associated with the user
 */
@Serializable
class UserResponse(
    val userId: Int,
    @Serializable(with = UUIDSerializer::class)
    val token: UUID,
)
