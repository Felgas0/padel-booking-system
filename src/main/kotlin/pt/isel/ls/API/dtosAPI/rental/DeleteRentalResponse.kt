package pt.isel.ls.API.dtosAPI.rental

import kotlinx.serialization.Serializable

/**
 * Represents the response returned after attempting to delete a rental.
 *
 * @property message a message describing the result of the operation
 * @property success indicates whether the deletion was successful
 */
@Serializable
class DeleteRentalResponse(
    val message: String,
    val success: Boolean,
)
