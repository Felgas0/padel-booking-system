package pt.isel.ls.API.dtosAPI.court

import kotlinx.serialization.Serializable

/**
 * Represents the response returned after creating a court.
 *
 * @property courtId the unique identifier of the newly created court
 */
@Serializable
class CourtResponse(val courtId: Int)
