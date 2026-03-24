package pt.isel.ls.services

import pt.isel.ls.storage.Storage

/**
 * Central entry point for accessing all service-layer logic.
 *
 * Each service encapsulates business rules and application logic for a specific domain
 * (users, clubs, courts, rentals), and delegates persistence to the [Storage] layer.
 *
 * This class wires together the service layer by injecting the shared storage dependency.
 *
 * @property usersServices handles user-related business logic
 * @property clubsServices handles club-related business logic
 * @property courtsServices handles court-related business logic
 * @property rentalsServices handles rental-related business logic
 */
open class Services(data: Storage) {
    val usersServices = UsersServices(data)
    val clubsServices = ClubsServices(data)
    val courtsServices = CourtsServices(data)
    val rentalsServices = RentalsServices(data)
}
