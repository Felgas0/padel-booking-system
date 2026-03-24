package pt.isel.ls.API.dtosAPI.rental

import kotlinx.serialization.Serializable

/**
 * Represents the response for a paginated list of rentals.
 *
 * @property rentals the list of rental details returned in the current page
 * @property totalCount the total number of rentals available
 */
@Serializable
data class RentalsListResponse(
    val rentals: List<RentalSearch.RentalDetails>,
    val totalCount: Int,
)
