package pt.isel.ls.storage.dtosStorage.rental

import java.time.LocalDateTime

/**
 * Represents the request body for updating an existing rental.
 *
 * @property date the new start date and time of the rental
 * @property duration the new duration of the rental in hours
 */
data class RentalEdit(
    val date: LocalDateTime,
    val duration: Int
)

