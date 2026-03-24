package pt.isel.ls.API.dtosAPI.user

import kotlinx.serialization.Serializable

/**
 * Represents the response for a paginated list of users.
 *
 * @property users the list of user details returned in the current page
 * @property totalCount the total number of users available
 */
@Serializable
data class UsersListResponse(
    val users: List<UserDetails>,
    val totalCount: Int,
)
