package pt.isel.ls.API.routersAPI

import org.http4k.core.Filter
import org.http4k.core.Method.*
import org.http4k.core.then
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import pt.isel.ls.API.RentalsAPI

/**
 * Defines the routing for rental-related endpoints.
 *
 * @param api the controller responsible for handling rental operations
 * @param auth the authentication filter applied to private routes
 * @return a RoutingHttpHandler with all rental-related routes
 */
fun rentalsRouter(api: RentalsAPI, auth: Filter): RoutingHttpHandler = routes(

    // rotas private
    auth.then("rentals"
            bind POST   to api::createRental),
    auth.then("rentals/{rentalId}"
            bind DELETE to api::deleteRental),
    auth.then("rentals/{rentalId}"
            bind PUT    to api::updateRental),

    // rotas public
    "rentals/{rentalId}"
            bind GET to api::getRentalDetails,
    "clubs/{clubId}/courts/{courtId}/available-hours"
            bind GET to api::getAvailableHours,
    "clubs/{clubId}/courts/{courtId}/rentals"
            bind GET to api::getRentals,
    "users/{userId}/rentals"
            bind GET to api::getRentalsByUser
)
