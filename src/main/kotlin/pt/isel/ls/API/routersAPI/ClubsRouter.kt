package pt.isel.ls.API.routersAPI

import org.http4k.core.Method.*
import org.http4k.routing.routes
import org.http4k.routing.bind
import org.http4k.core.Filter
import org.http4k.core.then
import org.http4k.routing.RoutingHttpHandler
import pt.isel.ls.API.ClubsAPI

/**
 * Defines the routing for club-related endpoints.
 *
 * @param api the controller responsible for handling club operations
 * @param auth the authentication filter applied to private routes
 * @return a RoutingHttpHandler with all club-related routes
 */
fun clubsRouter(api: ClubsAPI, auth: Filter): RoutingHttpHandler = routes(

    // rotas private
    auth.then("clubs"
            bind POST to api::createClub),

    // rotas public
    "clubs"
            bind GET to api::getAllClubs,
    "clubs/search"
            bind GET to api::searchClubs,
    "clubs/{clubId}"
            bind GET to api::getClubDetails,

    )
