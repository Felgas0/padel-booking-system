package pt.isel.ls.API.routersAPI

import org.http4k.core.Filter
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.then
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import pt.isel.ls.API.CourtsAPI

/**
 * Defines the routing for court-related endpoints.
 *
 * @param api the controller responsible for handling court operations
 * @param auth the authentication filter applied to private routes
 * @return a RoutingHttpHandler with all court-related routes
 */
fun courtsRouter(api: CourtsAPI, auth: Filter): RoutingHttpHandler = routes(

    // rotas private
    auth.then("courts"
            bind POST to api::createCourt),

    // rotas public
    "clubs/{clubId}/courts"
            bind GET to api::getCourts,
    "courts/{courtId}"
            bind GET to api::getCourtDetails
)
