package pt.isel.ls.API.dtosAPI.club

import kotlinx.serialization.Serializable

/**
 * Represents the response returned after creating a club.
 *
 * @property clubId the unique identifier of the newly created club
 */
@Serializable
data class ClubResponse(val clubId: Int)
