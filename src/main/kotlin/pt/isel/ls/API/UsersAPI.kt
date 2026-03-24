package pt.isel.ls.API

import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.path
import pt.isel.ls.API.dtosAPI.user.*
import pt.isel.ls.API.utils.*
import pt.isel.ls.services.UsersServices
import pt.isel.ls.utils.*

/**
 * Handles HTTP requests related to users.
 *
 * @property services the service layer that provides business logic for users
 */
class UsersAPI(
    private val services: UsersServices,
) : APIModel() {

    /**
     * Handles POST /users
     * Creates a new user with the provided registration data.
     *
     * @param req the HTTP request containing user creation data
     * @return a Response with status 201 Created and the user's ID and token
     */
    fun createUser(req: Request): Response {
        val body = Json.decodeFromString<UserCreate>(req.bodyString())
        val resp = services.createUser(body)
        return Response(Status.CREATED).json(resp)
    }

    /**
     * Handles POST /users/login
     * Authenticates a user with provided credentials.
     *
     * @param req the HTTP request containing login credentials
     * @return a Response with status 200 OK and the user's ID and token if successful
     */
    fun loginUser(req: Request): Response {
        val body = Json.decodeFromString<UserLogin>(req.bodyString())
        val resp = services.loginUser(body)
        return Response(Status.OK).json(resp)
    }

    /**
     * Handles GET /users/{userId}
     * Retrieves details of a user by ID.
     *
     * @param req the HTTP request containing the path parameter "userId"
     * @return a Response with status 200 OK and the user details
     */
    fun getUserDetails(req: Request): Response {
        val id = req.path("userId")?.toInt()
            ?: badRequest("userId must be provided")
        return Response(Status.OK).json(services.getUser(id))
    }

    /**
     * Handles GET /users?skip=&limit=
     * Retrieves a paginated list of users.
     *
     * @param req the HTTP request with optional "skip" and "limit" query parameters
     * @return a Response with status 200 OK and the list of users
     */
    fun getAllUsers(req: Request): Response {
        val (limit, skip) = paging(req)
        val page = services.getAllUsers(skip, limit)
        return Response(Status.OK).json(
            UsersListResponse(page.items, page.totalCount)
        )
    }
}
