package pt.isel.ls.storage

import pt.isel.ls.domain.User
import pt.isel.ls.storage.dtosStorage.user.UserInsert
import java.util.UUID

interface UserStorage {
    /**
     * @return returns all the users in the database
     */
    fun getAllUsers(): List<User>

    /**
     * @param id the id of the user to search for in the database
     * @return a user if the search found something or null if search found nothing in the database
     */
    fun getUserDetails(id: Int): User?

    /**
     * Creates a new user in the database using the provided user creation data.
     * @param user the user creation data
     * @return the created user
     */
    fun createUser(user: UserInsert): User

    /**
     * Retrieves a user from the database by their token.
     * @param token the user token
     * @return the corresponding user or null if not found
     */
    fun getUserByToken(token: UUID): User?

    /**
     * Checks whether the given email is already taken by another user.
     * @param email the email to check
     * @return true if the email is already used, false otherwise
     */
    fun isTaken(email: String): Boolean

    /**
     * Inserts a user directly into the database.
     * Used for seeding or internal operations.
     * @param user the user to insert
     */
    fun insertUser(user: User)

    /**
     * Deletes all users from the database.
     * Used for testing or reset operations.
     */
    fun reset()

    /**
     * Deletes a user from the database by their ID.
     * @param id the ID of the user to delete
     */
    fun deleteUserById(id: Int)

    /**
     * Retrieves a user from the database by their name.
     * @param name the name of the user
     * @return the corresponding user or null if not found
     */
    fun getUserByName(name: String): User?
}
