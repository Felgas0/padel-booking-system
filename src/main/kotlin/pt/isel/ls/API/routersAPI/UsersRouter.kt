package pt.isel.ls.API.routersAPI

import org.http4k.core.Method.*
import org.http4k.routing.routes
import org.http4k.routing.bind
import org.http4k.routing.RoutingHttpHandler
import pt.isel.ls.API.UsersAPI

/**
 * Defines the routing for user-related endpoints.
 *
 * @param api the controller responsible for handling user operations
 * @return a RoutingHttpHandler with all user-related routes
 */
fun usersRouter(api: UsersAPI): RoutingHttpHandler = routes(

    // rotas public
    "users"
            bind POST to api::createUser,
    "users"
            bind GET  to api::getAllUsers,
    "users/{userId}"
            bind GET to api::getUserDetails,
    "users/login"
            bind POST to api::loginUser
)
