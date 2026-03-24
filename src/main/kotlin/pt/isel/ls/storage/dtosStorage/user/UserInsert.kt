package pt.isel.ls.storage.dtosStorage.user

/**
 * Represents the data required to insert a new user into storage.
 *
 * @property name the user's full name
 * @property email the user's email address
 * @property password the user's plain text password (to be hashed before storage)
 */
data class UserInsert(
    val name: String,
    val email: String,
    val password: String
)

