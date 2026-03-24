package pt.isel.ls.API.dtosAPI.court

import kotlinx.serialization.Serializable

/**
 * Represents the response for a paginated list of courts.
 *
 * @property courts the list of court details returned in the current page
 * @property totalCount the total number of courts available
 */
@Serializable
data class CourtsListResponse(
    val courts: List<CourtDetails>,
    val totalCount: Int,
)
