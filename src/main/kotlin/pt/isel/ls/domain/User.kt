package pt.isel.ls.domain

import java.util.UUID

/**
 * Represents a user of the system.
 *
 * @property id the unique identifier of the user
 * @property name the name of the user
 * @property email the email address of the user
 * @property passwordHash the hashed password of the user
 * @property token the authentication token associated with the user
 *
 * @throws IllegalArgumentException if id is not positive, or if name or passwordHash are blank
 */
data class User(
    val id: Int,
    val name: String,
    val email: Email,
    val passwordHash: String,
    val token: UUID,
) {
    init {
        require(id > 0) { "id must be higher than zero: $id" }
        require(name.isNotBlank()) { "name must not be blank" }
        require(passwordHash.isNotBlank()) { "password must not be blank" }
    }
}
