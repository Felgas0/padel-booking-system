package pt.isel.ls.API

import pt.isel.ls.services.Services

/**
 * Central access point to the API layer, exposing all route-specific APIs.
 *
 * @property userAPI the API handler for user-related operations
 * @property clubsAPI the API handler for club-related operations
 * @property courtsAPI the API handler for court-related operations
 * @property rentalsAPI the API handler for rental-related operations
 */
class API(
    services: Services,
) : APIModel() {
    val userAPI = UsersAPI(services.usersServices)
    val clubsAPI = ClubsAPI(services.clubsServices)
    val courtsAPI = CourtsAPI(services.courtsServices)
    val rentalsAPI = RentalsAPI(services.rentalsServices)
}
