package pt.isel.ls.domain

import java.time.LocalDateTime

/**
 * Represents a rental of a court.
 *
 * @property id the unique identifier of the rental
 * @property date the starting date and time of the rental
 * @property duration the duration of the rental in hours
 * @property user the identifier of the user who made the rental
 * @property clubId the identifier of the club where the rental takes place
 * @property courtId the identifier of the court being rented
 *
 * @throws IllegalArgumentException if id, duration, clubId, or courtId are not positive
 */
data class Rental(
    val id: Int,
    val date: LocalDateTime,
    val duration: Int,
    val user: Int,
    val clubId: Int,
    val courtId: Int,
) {
    init {
        require(id > 0) { "Id must be greater than zero." }
        require(duration > 0) { "Duration greater than 0." }
        require(clubId > 0) { "clubId must exist and be greater than zero." }
        require(courtId > 0) { "courtId must exist and be greater than zero." }
    }
}
