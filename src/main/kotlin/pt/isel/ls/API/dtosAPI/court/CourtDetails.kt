package pt.isel.ls.API.dtosAPI.court

import kotlinx.serialization.Serializable

/**
 * Represents detailed information about a court.
 *
 * @property id the unique identifier of the court
 * @property name the name of the court
 * @property clubId the identifier of the club to which the court belongs
 */
@Serializable
class CourtDetails(
    val id: Int,
    val name: String,
    val clubId: Int,
)
