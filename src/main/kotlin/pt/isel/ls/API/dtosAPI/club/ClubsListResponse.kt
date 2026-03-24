package pt.isel.ls.API.dtosAPI.club

import kotlinx.serialization.Serializable

/**
 * Represents the response for a paginated list of clubs.
 *
 * @property clubs the list of club details returned in the current page
 * @property totalCount the total number of clubs available
 */
@Serializable
data class ClubsListResponse(
    val clubs: List<ClubDetails>,
    val totalCount: Int,
)
