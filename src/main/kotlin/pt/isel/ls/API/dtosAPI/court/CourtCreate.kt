package pt.isel.ls.API.dtosAPI.court

import kotlinx.serialization.Serializable

/**
 * Represents a request to create a new court.
 *
 * @property name the name of the court to be created
 * @property clubId the identifier of the club to which the court belongs
 */
@Serializable
data class CourtCreate(val name: String, val clubId: Int)
