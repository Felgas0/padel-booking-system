package pt.isel.ls.storage.dataMem

import org.springframework.security.crypto.bcrypt.BCrypt
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.User
import pt.isel.ls.storage.UserStorage
import pt.isel.ls.storage.dtosStorage.user.UserInsert
import java.util.*

/**
 * In-memory implementation of UserStorage for testing or development purposes.
 * Stores data in maps instead of a persistent database.
 *
 * Uses two maps to allow efficient lookup by both user ID and authentication token.
 * Suitable for use in unit tests or local development where persistence is not required.
 */
class UsersMem : UserStorage {

    private val users = mutableMapOf<Int, User>()
    private val tokenToUser = mutableMapOf<UUID, User>() // Secondary index by token

    /**
     * Retrieves all users currently stored in memory.
     */
    override fun getAllUsers(): List<User> = users.values.toList()

    /**
     * Returns user details by user ID, or null if not found.
     */
    override fun getUserDetails(id: Int): User? = users[id]

    /**
     * Creates a new user with a hashed password and generated token.
     * Automatically assigns the next available integer ID.
     */
    override fun createUser(user: UserInsert): User {
        val id = (users.keys.maxOrNull() ?: 0) + 1
        val token = UUID.randomUUID()
        val hash = BCrypt.hashpw(user.password, BCrypt.gensalt())

        val newUser = User(id, user.name, Email(user.email), hash, token)
        users[id] = newUser
        tokenToUser[token] = newUser

        return newUser
    }

    /**
     * Finds a user by their unique authentication token.
     */
    override fun getUserByToken(token: UUID): User? = tokenToUser[token]

    /**
     * Checks if the given email address is already registered.
     */
    override fun isTaken(email: String): Boolean =
        users.values.any { it.email.value == email }

    /**
     * Inserts a fully defined user into memory. Used for test setup.
     */
    override fun insertUser(user: User) {
        users[user.id] = user
        tokenToUser[user.token] = user
    }

    /**
     * Clears all users from memory. Typically used to reset state between tests.
     */
    override fun reset() {
        users.clear()
        tokenToUser.clear()
    }

    /**
     * Deletes a user by ID. Token index is not updated here, assuming test scope usage.
     */
    override fun deleteUserById(id: Int) {
        users.remove(id)
    }

    /**
     * Searches for a user by exact name match.
     */
    override fun getUserByName(name: String): User? =
        users.values.find { it.name == name }
}
