package pt.isel.ls.storage.dtosStorage.rental

import java.time.LocalDateTime

/**
 * Represents the data required to insert a new rental into storage.
 *
 * @property clubId the identifier of the club where the rental will occur
 * @property courtId the identifier of the court to be rented
 * @property date the start date and time of the rental
 * @property duration the duration of the rental in hours
 */
data class RentalInsert(
    val clubId: Int,
    val courtId: Int,
    val date: LocalDateTime,
    val duration: Int
)
