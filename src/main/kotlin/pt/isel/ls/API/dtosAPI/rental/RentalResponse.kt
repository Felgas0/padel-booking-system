package pt.isel.ls.API.dtosAPI.rental

import kotlinx.serialization.Serializable

/**
 * Represents the response returned after creating a rental.
 *
 * @property rentalId the unique identifier of the newly created rental
 */
@Serializable
class RentalResponse(val rentalId: Int)
