package pt.isel.ls.API.dtosAPI.rental

import kotlinx.serialization.Serializable

/**
 * Represents the response returned after attempting to update a rental.
 *
 * @property message a message describing the result of the update operation
 * @property success indicates whether the update was successful
 */
@Serializable
class RentalUpdateResponse(
    val message: String,
    val success: Boolean,
)
