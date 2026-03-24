package pt.isel.ls.API.dtosAPI.club

import kotlinx.serialization.Serializable

/**
 * Represents a request to create a new club.
 *
 * @property name the name of the club to be created
 */
@Serializable
data class ClubCreate(val name: String)
