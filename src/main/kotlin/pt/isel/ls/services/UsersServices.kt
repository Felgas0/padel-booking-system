package pt.isel.ls.services

import org.springframework.security.crypto.bcrypt.BCrypt
import pt.isel.ls.API.dtosAPI.user.*
import pt.isel.ls.services.dtosServices.Paged
import pt.isel.ls.storage.Storage
import pt.isel.ls.storage.dtosStorage.user.UserInsert
import pt.isel.ls.utils.*

/**
 * Service class responsible for user-related operations such as registration,
 * authentication, and data retrieval. Encapsulates validation and business rules.
 *
 * @param storage the shared data access layer
 */
class UsersServices(storage: Storage) : ServicesModel(storage) {

    /**
     * Creates a new user after validating the input data and checking for duplicates.
     *
     * @param body the user creation input containing name, email, and password
     * @return a response containing the new user's ID and token
     */
    fun createUser(body: UserCreate): UserResponse {
        requireName(body.name)
        requireName(body.email, "Email")
        requireName(body.password, "Password")

        if (body.password.length !in 5..25)
            badRequest("Password must have between 5 and 25 characters.")

        if (!body.email.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")))
            badRequest("Email format is invalid.")


        if (storage.userStorage.isTaken(body.email))
            conflict("A user with that email already exists.")

        val insert = UserInsert(
            name = body.name,
            email = body.email,
            password = body.password
        )
        val user = storage.userStorage.createUser(insert)
        return UserResponse(user.id, user.token)
    }

    /**
     * Authenticates a user using name and password.
     *
     * @param body the login credentials
     * @return a response containing the authenticated user's ID and token
     */
    fun loginUser(body: UserLogin): AuthResponse {
        requireName(body.name)
        requireName(body.password, "Password")

        val user = storage.userStorage.getUserByName(body.name)
            ?: unauthorized("Name or password invalid.")

        if (!BCrypt.checkpw(body.password, user.passwordHash))
            unauthorized("Name or password invalid.")

        return AuthResponse(user.id, user.token.toString())
    }

    /**
     * Retrieves user details by ID.
     *
     * @param id the user's unique identifier
     * @return the full user details (excluding password/token)
     */
    fun getUser(id: Int): UserDetails {
        requireId(id, "User id")
        val user = userOr404(id)
        return UserDetails(user.id, user.name, user.email)
    }

    /**
     * Retrieves all users in paginated format.
     *
     * @param skip the number of users to skip
     * @param limit the number of users to retrieve
     * @return a paged result of user details
     */
    fun getAllUsers(skip: Int, limit: Int): Paged<UserDetails> =
        storage.userStorage.getAllUsers()
            .map { UserDetails(it.id, it.name, it.email) }
            .page(skip, limit)
}
