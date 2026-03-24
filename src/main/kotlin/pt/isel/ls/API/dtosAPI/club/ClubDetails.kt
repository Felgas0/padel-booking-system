package pt.isel.ls.API.dtosAPI.club

import kotlinx.serialization.Serializable

/**
 * Represents detailed information about a club.
 *
 * @property id the unique identifier of the club
 * @property name the name of the club
 * @property ownerId the unique identifier of the user who owns the club
 */
@Serializable
class ClubDetails(
    val id: Int,
    val name: String,
    val ownerId: Int,
)
